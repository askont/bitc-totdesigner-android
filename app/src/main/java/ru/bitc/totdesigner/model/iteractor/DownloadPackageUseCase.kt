package ru.bitc.totdesigner.model.iteractor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import ru.bitc.totdesigner.model.entity.loading.AllLoadingJob
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase(private val repository: DownloadPackageRepository) {

    private val jobsLoading = mutableMapOf<String, Deferred<Flow<LoadingPackage>>>()

    fun loadPackage(lessonUrl: String): Flow<AllLoadingJob> {
        val async = CoroutineScope(Dispatchers.IO).async {
            repository.downloadPackage(lessonUrl)
        }
        if (!jobsLoading.containsKey(lessonUrl)){
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