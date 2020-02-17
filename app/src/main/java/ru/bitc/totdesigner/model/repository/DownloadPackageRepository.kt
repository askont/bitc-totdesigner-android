package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import timber.log.Timber

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageRepository(
    private val api: SoapApi,
    private val dispatcher: DispatcherProvider
) {

    suspend fun downloadPackage(lessonUrl: String): Flow<LoadingPackage> = flow {
        emit(LoadingPackage.Loading(lessonUrl, 2000))
        try {
            api.downloadLessonPackage(lessonUrl)
            emit(LoadingPackage.Finish(lessonUrl))
        } catch (e: Exception) {
            emit(LoadingPackage.Error(lessonUrl, "Error download"))
            Timber.e(e)
        }
    }.flowOn(dispatcher.io)


}