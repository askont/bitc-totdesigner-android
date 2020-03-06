package ru.bitc.totdesigner.model.converter

import com.tickaroo.tikxml.TikXml
import okio.Buffer
import ru.bitc.totdesigner.model.database.dto.LessonPath
import ru.bitc.totdesigner.model.database.dto.absolutePath
import ru.bitc.totdesigner.model.entity.SavedLesson
import ru.bitc.totdesigner.model.models.SavedLessonPreview
import java.io.*

/**
 * Created on 04.03.2020
 * @author YWeber */

class SavedLessonModelConverter :
    ConverterXmlToModel<SavedLessonPreview> {
    private val build = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    override fun convertFileToModel(file: File): SavedLessonPreview {
        val fileReader: Reader = BufferedReader(InputStreamReader(FileInputStream(file)))
        return build.read(Buffer().writeUtf8(fileReader.readText()), SavedLessonPreview::class.java)
    }

    fun convertToEntity(path: LessonPath, xmlModel: SavedLessonPreview): SavedLesson {
        val images = xmlModel.images.map { path.absolutePath(it.nameImage) }
        return SavedLesson(
            path.lessonLocalPath,
            path.lessonRemoteUrl,
            xmlModel.nameLesson,
            xmlModel.description,
            path.absolutePath(xmlModel.mainImage),
            images
        )
    }

}