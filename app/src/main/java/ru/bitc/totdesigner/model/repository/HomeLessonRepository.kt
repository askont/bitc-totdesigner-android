package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.bitc.totdesigner.model.converter.SavedLessonModelConverter
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.database.dto.LessonPath
import ru.bitc.totdesigner.model.entity.SavedLesson
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.path.PathManager
import java.io.File

/**
 * Created on 03.03.2020
 * @author YWeber */

class HomeLessonRepository(
    private val pathDao: PathDao,
    private val dispatcher: DispatcherProvider,
    private val pathManager: PathManager,
    private val converterXmlToModel: SavedLessonModelConverter
) {

    private var currentPath: LessonPath? = null

    fun findLessonByRemotePath(remotePath: String) = flow {
        val localPath = pathDao.findLessonPath(remotePath)
        currentPath = localPath
        val previewFile = previewFile(localPath.lessonLocalPath)
        val xml = converterXmlToModel.loadXmlToModel(previewFile)
        emit(converterXmlToModel.convertToEntity(localPath, xml))
    }.flowOn(dispatcher.io)

    fun getSaveLesson() = flow {
        val map = pathDao.gelAllPath().map(::mapEntity)
        emit(map)
    }.flowOn(dispatcher.io)

    private fun mapEntity(lessonPath: LessonPath): SavedLesson {
        val previewFile = previewFile(lessonPath.lessonLocalPath)
        val xml = converterXmlToModel.loadXmlToModel(previewFile)
        return converterXmlToModel.convertToEntity(lessonPath, xml)
    }

    private fun previewFile(localPathDir: String) =
        File(localPathDir + File.separator + pathManager.preview)

    fun deleteSaveLesson() {
        CoroutineScope(dispatcher.io).launch {
            pathDao.deletePath(currentPath ?: return@launch)
            val lessonDir = File(currentPath?.lessonLocalPath ?: "")
            lessonDir.deleteRecursively()
        }
    }
}
