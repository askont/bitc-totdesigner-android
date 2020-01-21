package ru.bitc.totdesigner.model.repository

import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.http.SoapApi

/*
 * Created on 2019-12-19
 * @author YWeber
 */
class LessonRepository(private val api: SoapApi) {

    suspend fun getPreviewLessons(): PreviewLessons {
        val lessons = api.getLessonsPreview()
        return PreviewLessons(lessons.lessonsInfo.map {
            PreviewLessons.Lesson(it.name, it.previewIcon)
        })
    }
}