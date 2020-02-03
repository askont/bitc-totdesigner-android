package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
            progressLoading(lessonUrl, jobLoad, System.currentTimeMillis(), 1, 10)
            emit(LoadingPackage.Loading(lessonUrl, 100))
            emit(LoadingPackage.Finish(lessonUrl))
        } catch (e: Exception) {
            emit(LoadingPackage.Error(lessonUrl, "Error download"))
        }

    }.flowOn(dispatcher.io)


    private suspend fun FlowCollector<LoadingPackage>.progressLoading(
        urlId: String,
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
                    emit(LoadingPackage.Loading(urlId, progressLoading))
                }else{
                    progressLoading = startProgress
                    emit(LoadingPackage.Loading(urlId, progressLoading))
                }

            }

        }
    }
}