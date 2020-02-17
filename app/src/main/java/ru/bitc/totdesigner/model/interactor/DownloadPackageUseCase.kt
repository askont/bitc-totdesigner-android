package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import ru.bitc.totdesigner.model.entity.loading.AllLoadingJob
import ru.bitc.totdesigner.model.entity.loading.LoadingDetailed
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase(
    private val repository: DownloadPackageRepository,
    private val lessonRepository: LessonRepository
) {

    private val jobsLoading = mutableMapOf<String, Deferred<Flow<LoadingPackage>>>()

    fun getListLoadingPackage() = jobsLoading.values.asFlow()
        .map { it.getCompleted() }
        .flattenConcat()
        .map { loading ->
            lessonRepository
                .getPreviewLessons()
                .previews.filter { it.lessonUrl == loading.urlId }
                .map { LoadingDetailed.Loading(it.lessonUrl, it.imageUrl, it.title) }
        }

    fun getAllLoadingPackage(lessonUrl: String): Flow<AllLoadingJob> {
        val async = CoroutineScope(Dispatchers.IO).async {
            repository.downloadPackage(lessonUrl)
        }
        if (!jobsLoading.containsKey(lessonUrl)) {
            jobsLoading[lessonUrl] = async
        }
        return flow {
            jobsLoading.map { it.value.await() }.asFlow()
                .flattenConcat()
                .onEach {
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

    fun cancelAllJob() {
        jobsLoading.values.forEach { job ->
            job.cancel()
        }
        jobsLoading.clear()
    }
}