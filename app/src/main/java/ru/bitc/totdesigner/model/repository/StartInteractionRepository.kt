package ru.bitc.totdesigner.model.repository

import kotlinx.coroutines.flow.flow
import ru.bitc.totdesigner.model.converter.InteractionModelConverter
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.system.path.PathManager
import java.io.File

/**
 * Created on 12.03.2020
 * @author YWeber */

class StartInteractionRepository(
    private val pathDao: PathDao,
    private val pathManager: PathManager,
    private val converter: InteractionModelConverter
) {


    suspend fun getStartLesson(lessonPath: String) = flow {
        val lesson = pathDao.findLessonPath(lessonPath)
        val file = File(lesson.lessonLocalPath + File.separator + pathManager.setting)
        val xml = converter.loadXmlToModel(file)
        emit(converter.convertToEntity(lesson.lessonLocalPath, xml))
    }

}