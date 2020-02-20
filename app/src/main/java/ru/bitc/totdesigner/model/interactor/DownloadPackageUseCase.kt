package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.*
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

    fun getCountAllLoadingPackage(lessonUrl: String): Flow<AllLoadingJob> {
        val async = CoroutineScope(dispatcher.io).async {
            repository.downloadPackage(lessonUrl)
        }
        if (!jobsLoading.containsKey(lessonUrl)) {
            jobsLoading[lessonUrl] = async
        }

        return flow {
            jobsLoading.map { it.value.await() }.asFlow()
                .flattenMerge()
                .onEach { loads -> handleEventLoading(loads) }
                .map { jobsLoading.size }
                .distinctUntilChanged()
                .collect { size ->
                    if (size == 0) {
                        emit(AllLoadingJob.Finish)
                    } else {
                        emit(AllLoadingJob.Progress(size, 2000 * size))
                    }
                }
        }
    }

    private fun handleEventLoading(loads: LoadingPackage) {
        if (!previewLoading.contains(loads)) {
            previewLoading.add(loads)
            eventUpdateFlow.offer(true)
        }
        if (loads is LoadingPackage.Finish) {
            jobsLoading.remove(loads.urlId)
            val loading = previewLoading
                .filterIsInstance<LoadingPackage.Loading>().firstOrNull { it.urlId == loads.urlId }
            previewLoading.remove(loading ?: return)
            eventUpdateFlow.offer(true)
        }
    }

    suspend fun getListPairLoadingAndPreview(): Flow<List<Pair<LoadingPackage, PreviewLessons.Lesson>>> {
        val lessonsPreview = lessonRepository.getPreviewLessons()
        return eventUpdateFlow.asFlow()
            .map {
                previewLoading.map { load ->
                    load to lessonsPreview
                        .previews
                        .firstOrNull { load.urlId == it.lessonUrl }
                }.filterIsInstance<Pair<LoadingPackage, PreviewLessons.Lesson>>()
            }

    }

    fun deleteJobByKey(urlId: String) {
        CoroutineScope(dispatcher.default).launch {
            jobsLoading[urlId]?.cancelAndJoin()
        }
        jobsLoading.remove(urlId)
        val loading = previewLoading
            .filterIsInstance<LoadingPackage.Loading>()
            .firstOrNull { it.urlId == urlId }
        if (loading != null) {
            previewLoading.remove(loading)
            eventUpdateFlow.offer(true)
        }


    }


    fun cancelAllJob() {
        jobsLoading.values.forEach { job ->
            job.cancel()
        }
        previewLoading.clear()
        jobsLoading.clear()
        eventUpdateFlow.offer(true)
    }
}