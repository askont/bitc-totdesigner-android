package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.database.dto.LessonPath
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.path.PathManager
import ru.bitc.totdesigner.system.zip.UnpackingZip
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.util.concurrent.CancellationException

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageRepository(
    private val api: SoapApi,
    private val path: PathManager,
    private val unzip: UnpackingZip,
    private val pathDao: PathDao,
    private val dispatcher: DispatcherProvider
) {

    fun downloadPackage(lessonUrl: String, lessonName: String) = flow<LoadingPackage> {
        val packageZip = api.downloadLessonPackage(lessonUrl)
        val zipFile = writeZipToDevices(
            packageZip.byteStream(), lessonName
        )
        val localPath = unzip.unpackingFile(zipFile.toString(), lessonName)
        pathDao.insertPath(LessonPath(lessonUrl, localPath))
        zipFile.delete()
        emit(LoadingPackage.Finish(lessonUrl))
    }.onStart {
        emit(LoadingPackage.Loading(lessonUrl, 10000))
    }.catch {
        if (it is CancellationException) {
            Timber.e(it)
            throw CancellationException()
        }
        emit(LoadingPackage.Error(lessonUrl, "Error download"))
        Timber.e(it)

    }.flowOn(dispatcher.io)

    private fun writeZipToDevices(zipByteStream: InputStream, fileName: String): File {
        val filePathDir = File(path.externalDirLocalFile + File.separator + "$fileName.${path.zipType}")
        zipByteStream.use { responseStream ->
            filePathDir.outputStream().use { outputFile ->
                responseStream.copyTo(outputFile, 512)
            }
        }
        return filePathDir
    }


}