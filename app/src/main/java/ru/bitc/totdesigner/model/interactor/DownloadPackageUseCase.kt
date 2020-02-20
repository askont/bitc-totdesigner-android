package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.AllLoadingJob
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.system.flow.DispatcherProvider

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase(
    private val repository: DownloadPackageRepository,
    private val lessonRepository: LessonRepository,
    private val dispatcher: DispatcherProvider
) {
    private val jobsLoading = mutableMapOf<String, Deferred<Flow<LoadingPackage>>>()
    private val previewLoading = mutableListOf<LoadingPackage>()
    private val eventUpdateFlow = ConflatedBroadcastChannel<Boolean>()

    fun getCountAllLoadingPackage(lessonUrl: String, isDelete: Boolean): Flow<AllLoadingJob> {
        val async = CoroutineScope(dispatcher.ui).async {
            repository.downloadPackage(lessonUrl)
        }
        if (!jobsLoading.containsKey(lessonUrl)) {
            jobsLoading[lessonUrl] = async
        } else if (isDelete) deleteDuplicateJob(lessonUrl)

        return if (jobsLoading.isNotEmpty()) {
            jobsLoading.values.asFlow()
                .map { it.await() }
                .flattenMerge()
                .onEach { loads -> handleEventLoading(loads) }
                .distinctUntilChanged()
                .map { if (it is LoadingPackage.Error) -1 else jobsLoading.size }
                .map { createEntityJob(it) }
        } else flow { emit(AllLoadingJob.Finish) }
    }

    private fun deleteDuplicateJob(lessonUrl: String) {
        jobsLoading[lessonUrl]?.cancel()
        jobsLoading.remove(lessonUrl)
        val loading = previewLoading
            .filterIsInstance<LoadingPackage.Loading>()
            .firstOrNull { it.urlId == lessonUrl }
        if (loading != null) {
            previewLoading.remove(loading)
            eventUpdateFlow.offer(true)
        }
    }

    private fun handleEventLoading(loads: LoadingPackage) {
        if (!previewLoading.contains(loads)) {
            previewLoading.add(loads)
        }
        correctingEventLoading(loads)
    }

    private fun correctingEventLoading(loads: LoadingPackage) {
        if (loads !is LoadingPackage.Loading) {
            jobsLoading.remove(loads.urlId)
            val loading = previewLoading
                .filterIsInstance<LoadingPackage.Loading>().firstOrNull { it.urlId == loads.urlId }
            previewLoading.remove(loading ?: return)
        }
        eventUpdateFlow.offer(true)
    }

    private fun createEntityJob(it: Int): AllLoadingJob = when (it) {
        0 -> AllLoadingJob.Finish
        -1 -> AllLoadingJob.Error
        else -> AllLoadingJob.Progress(it, 2000 * it)
    }


    suspend fun getListPairLoadingAndPreview(): Flow<List<Pair<LoadingPackage, PreviewLessons.Lesson>>> {
        val lessonsPreview = lessonRepository.getPreviewLessons()
        return eventUpdateFlow.asFlow()
            .map {
                previewLoading
                    .map { load -> createPairLessonAndLoading(load, lessonsPreview) }
                    .filterIsInstance<Pair<LoadingPackage, PreviewLessons.Lesson>>()
            }
    }

    private fun createPairLessonAndLoading(
        load: LoadingPackage,
        lessonsPreview: PreviewLessons
    ): Pair<LoadingPackage, PreviewLessons.Lesson?> {
        return load to lessonsPreview
            .previews
            .firstOrNull { load.urlId == it.lessonUrl }
    }

    fun cancelAllJob() {
        jobsLoading.values.forEach { job ->
            job.cancel()
        }
        val notLoadingList = previewLoading.filter { it !is LoadingPackage.Loading }
        previewLoading.clear()
        previewLoading.addAll(notLoadingList)
        jobsLoading.clear()
        eventUpdateFlow.offer(true)
    }
}