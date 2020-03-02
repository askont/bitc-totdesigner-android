package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.path.PathManager
import ru.bitc.totdesigner.system.zip.UnpackingZip
import timber.log.Timber
import java.io.File
import java.io.InputStream

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageRepository(
    private val api: SoapApi,
    private val path: PathManager,
    private val unzip: UnpackingZip
) {

    fun downloadPackage(lessonUrl: String, lessonName: String): Flow<LoadingPackage> = flow {
        emit(LoadingPackage.Loading(lessonUrl, 10000))
        try {
            val packageZip = api.downloadLessonPackage(lessonUrl)
            val zipFile = writeZipToDevices(
                packageZip.byteStream(), lessonName
            )
            unzip.unpackingFile(zipFile.toString(), lessonName)
            zipFile.delete()
            emit(LoadingPackage.Finish(lessonUrl))
        } catch (e: Exception) {
            emit(LoadingPackage.Error(lessonUrl, "Error download"))
            Timber.e(e)
        }
    }

    private fun writeZipToDevices(zipByteStream: InputStream, fileName: String): File {
        val filePathDir = File(path.externalDirLocalFile + File.separator + "$fileName.${path.zipType}")
        zipByteStream.use { responseStream ->
            filePathDir.outputStream().use { outputFile ->
                responseStream.copyTo(outputFile, 4096)
            }
        }
        return filePathDir
    }


}