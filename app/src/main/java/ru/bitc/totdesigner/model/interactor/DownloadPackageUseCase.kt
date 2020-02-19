package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
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
    private val eventLoading = mutableListOf<LoadingPackage>()
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
                .onEach {
                    if (!eventLoading.contains(it)) {
                        eventLoading.add(it)
                    }
                    if (it is LoadingPackage.Finish) {
                        jobsLoading.remove(it.urlId)
                    }
                }
                .map { jobsLoading.size }
                .distinctUntilChanged()
                .collect {
                    if (it == 0) {
                        emit(AllLoadingJob.Finish)
                    } else {
                        emit(AllLoadingJob.Progress(it, 2000 * it))
                    }
                }
        }
    }

    suspend fun getListLoadingPackageJob(): Flow<Pair<LoadingPackage, PreviewLessons.Lesson>> {
        val lessonsPreview = lessonRepository.getPreviewLessons()
        return eventLoading.asFlow()
            .map { load ->
                load to lessonsPreview
                    .previews
                    .firstOrNull { load.urlId == it.lessonUrl }
            }
            .filterIsInstance()
    }

    fun deleteJobByKey(urlId: String) {
        if (jobsLoading.contains(urlId)) {
            jobsLoading[urlId]?.cancel()
            jobsLoading.remove(urlId)
        }

    }


    fun cancelAllJob() {
        jobsLoading.values.forEach { job ->
            job.cancel()
        }
        jobsLoading.clear()
    }
}