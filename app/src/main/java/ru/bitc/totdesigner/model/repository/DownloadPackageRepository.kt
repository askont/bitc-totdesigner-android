package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import ru.bitc.totdesigner.model.entity.LoadingPackage
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.flow.DispatcherProvider

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageRepository(
    private val api: SoapApi,
    private val dispatcher: DispatcherProvider
) {

    fun downloadPackage(lessonUrl: String): Flow<LoadingPackage> = flow<LoadingPackage> {

        val jobLoad = CoroutineScope(Dispatchers.IO).async {
            delay(1000)
            api.downloadLessonPackage(lessonUrl)

        }
        progressLoading(jobLoad, System.currentTimeMillis(), 1, 10)
        emit(LoadingPackage.Finish)
    }.flowOn(dispatcher.io)


    private suspend fun FlowCollector<LoadingPackage>.progressLoading(
        jobLoad: Deferred<ResponseBody>,
        startTime: Long,
        startProgress: Int,
        step: Int
    ) {
        var start = startTime
        var progressLoading = startProgress
        while (jobLoad.isActive) {
            val end = System.currentTimeMillis()
            if (end - start > 300) {
                start = end
                progressLoading += step - startProgress
                if (progressLoading <= 100) {
                    emit(LoadingPackage.Loading(progressLoading))
                }

            }

        }
    }


    fun loading() = (1..5).asFlow()
        .map { LoadingPackage.Loading(it) }
        .onEach { delay(200) }

    fun download(lessonUrl: String) = flow {
        api.downloadLessonPackage(lessonUrl)
        emit(LoadingPackage.Finish)
    }
}