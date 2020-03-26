package ru.bitc.totdesigner.system.zip

import ru.bitc.totdesigner.system.path.PathManager
import ru.bitc.totdesigner.system.printDebug
import java.io.File
import java.util.zip.ZipFile

/**
 * Created on 02.03.2020
 * @author YWeber */

class UnpackingZip(private val pathManager: PathManager) {
    suspend fun unpackingFile(locationZip: String, nameDir: String): String {
        val locationContentDir = File(pathManager.externalDirLocalFile + File.separator + nameDir)
        createContentDir(locationContentDir)
        unzipFile(locationZip, locationContentDir)
        return locationContentDir.toString()
    }

    private fun createContentDir(locationDirContent: File) {
        if (!locationDirContent.isDirectory) {
            locationDirContent.mkdir()
        }
    }


    private fun unzipFile(locationZip: String, locationContentDir: File) {
        ZipFile(locationZip).use { zip ->
            zip.entries().asSequence().forEach {
                zip.getInputStream(it).use { input ->
                    val targetPath = locationContentDir.absolutePath + File.separator + it.name
                    targetPath.printDebug()
                    File(targetPath).outputStream().use { output ->
                        input.copyTo(output,512)
                    }
                }
            }
        }
    }
}