package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.entity.loading.ProcessDownloading
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import timber.log.Timber

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase(
    private val repository: DownloadPackageRepository,
    private val lessonRepository: LessonRepository,
    private val dispatcher: DispatcherProvider
) {
    private val jobsLoading = mutableMapOf<String, Job>()
    private val previewLoading = mutableListOf<LoadingPackage>()
    private val eventUpdateFlow = ConflatedBroadcastChannel<Boolean>()
    private val eventLoading = ConflatedBroadcastChannel<LoadingPackage>()


    suspend fun processTaskEventLoadingCount(
        lessonUrl: String,
        lessonName: String,
        isDelete: Boolean
    ): Flow<ProcessDownloading> {
        if (!jobsLoading.containsKey(lessonUrl)) {
            jobsLoading[lessonUrl] = CoroutineScope(dispatcher.ui).launch {
                try {
                    repository.downloadPackage(lessonUrl, lessonName)
                        .collect {
                            handleEventLoading(it)
                            eventLoading.offer(it)
                        }
                } catch (ex: CancellationException) {
                    eventLoading.offer(LoadingPackage.Cancel(lessonUrl))
                    Timber.e("cancel use case")
                }
            }

        } else if (isDelete) deleteDuplicateJob(lessonUrl)

        return if (jobsLoading.isNotEmpty()) {
            eventLoading.asFlow()
                .map { if (it is LoadingPackage.Error) -1 else jobsLoading.size }
                .map { createEntityJob(it) }
                .flowOn(dispatcher.ui)
        } else flow { emit(ProcessDownloading.Finish) }
    }

    private fun deleteDuplicateJob(lessonUrl: String) {
        jobsLoading[lessonUrl]?.cancel()
        jobsLoading.remove(lessonUrl)
        val loading = previewLoading
            .filterIsInstance<LoadingPackage.Loading>()
            .filter { it.urlId == lessonUrl }
        if (loading.isNotEmpty()) {
            previewLoading.removeAll(loading)
        }
        eventUpdateFlow.offer(true)
    }

    private fun handleEventLoading(loads: LoadingPackage) {
        if (!previewLoading.contains(loads)) {
            previewLoading.add(loads)
        }
        correctingEventLoading(loads)
        eventUpdateFlow.offer(true)
    }

    private fun correctingEventLoading(loads: LoadingPackage) {
        if (loads !is LoadingPackage.Loading) {
            jobsLoading.remove(loads.urlId)
            val loading = previewLoading
                .filterIsInstance<LoadingPackage.Loading>().filter { it.urlId == loads.urlId }
            previewLoading.removeAll(loading)
        }
    }

    private fun createEntityJob(it: Int): ProcessDownloading = when (it) {
        0 -> ProcessDownloading.Finish
        -1 -> ProcessDownloading.Error
        else -> ProcessDownloading.Count(it, 2000 * it * 2)
    }


    /**
     * hot list observe, when change lessonsPreview should new event pair
     * */
    fun eventListPairProcessLoadingAndPreview(): Flow<List<Pair<LoadingPackage, PreviewLessons.Lesson>>> {
        val lessonsPreview = CoroutineScope(dispatcher.ui)
            .async { lessonRepository.getAllRemoteLesson() }
        return eventUpdateFlow.asFlow()
            .map { lessonsPreview.await() }
            .map {
                previewLoading
                    .map { load -> createPairLessonAndLoading(load, it) }
                    .filterIsInstance<Pair<LoadingPackage, PreviewLessons.Lesson>>()
            }
    }

    private fun createPairLessonAndLoading(
        load: LoadingPackage?,
        lessonsPreview: PreviewLessons
    ): Pair<LoadingPackage?, PreviewLessons.Lesson?> {
        return load to lessonsPreview
            .previews
            .firstOrNull { load?.urlId == it.lessonUrl }
    }

    fun cancelAllJob() {
        jobsLoading.values.forEach { job ->
            job.cancel()
        }
        val notLoadingList = previewLoading.filter { it !is LoadingPackage.Loading }
        previewLoading.clear()
        jobsLoading.clear()
        eventUpdateFlow.offer(true)
        previewLoading.addAll(notLoadingList)

    }

    fun clearFinishAndErrorType() {
        val loading = previewLoading.filterIsInstance<LoadingPackage.Loading>()
        previewLoading.clear()
        previewLoading.addAll(loading)
        eventUpdateFlow.offer(true)
    }
}