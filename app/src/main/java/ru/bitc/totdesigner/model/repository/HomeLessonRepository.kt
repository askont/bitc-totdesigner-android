package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.bitc.totdesigner.model.converter.SavedLessonModelConverter
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.database.dto.LessonPath
import ru.bitc.totdesigner.model.entity.SavedLesson
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import java.io.File

/**
 * Created on 03.03.2020
 * @author YWeber */

class HomeLessonRepository(
    private val pathDao: PathDao,
    private val dispatcher: DispatcherProvider,
    private val converterXmlToModel: SavedLessonModelConverter
) {

    fun getSaveLesson() = flow {
        val map = pathDao.gelAllPath().map(::mapEntity)
        emit(map)
    }.flowOn(dispatcher.io)

    private fun mapEntity(lessonPath: LessonPath): SavedLesson {
        val localPath = File(lessonPath.lessonLocalPath + File.separator + FILE_PREVIEW_NAME)
        val xml = converterXmlToModel.convertFileToModel(localPath)
        return converterXmlToModel.convertToEntity(lessonPath, xml)
    }

    private companion object {
        const val FILE_PREVIEW_NAME = "Preview.xml"
    }

}
