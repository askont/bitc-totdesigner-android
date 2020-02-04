package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        try {
            val jobLoad = CoroutineScope(Dispatchers.IO).async {
                api.downloadLessonPackage(lessonUrl)
            }
            progressLoading(lessonUrl, jobLoad, 1, 10)
            emit(LoadingPackage.Loading(lessonUrl, 100))
            delay(200)
            emit(LoadingPackage.Finish(lessonUrl))
        } catch (e: Exception) {
            emit(LoadingPackage.Error(lessonUrl, "Error download"))
        }
    }.flowOn(dispatcher.io)


    private suspend fun FlowCollector<LoadingPackage>.progressLoading(
        urlId: String,
        jobLoad: Deferred<ResponseBody>,
        startProgress: Int,
        step: Int
    ) {
        var progressLoading = startProgress
        while (jobLoad.isActive) {
            delay(300)
            progressLoading += step - startProgress
            if (progressLoading <= 100) {
                emit(LoadingPackage.Loading(urlId, progressLoading))
            }else{
                progressLoading = startProgress
                emit(LoadingPackage.Loading(urlId, progressLoading))
            }

        }
    }
}