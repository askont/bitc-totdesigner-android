package ru.bitc.totdesigner.model.repository

import ru.bitc.totdesigner.model.converter.ModelLessonToEntityPreviewConverter
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.model.models.Lessons

/*
 * Created on 2019-12-19
 * @author YWeber
 */
class LessonRepository(
    private val api: SoapApi,
    private val toEntityPreviewConverter: ModelLessonToEntityPreviewConverter
) {

    private var cacheLessons: Lessons? = null

    suspend fun getPreviewLessons(): PreviewLessons {
        val lessons = getLessonsCacheOrRemote()
        return toEntityPreviewConverter.convertModelToEntity(lessons)
    }

    private suspend fun getLessonsCacheOrRemote(): Lessons {
        val lessons = cacheLessons ?: api.getLessonsPreview()
        cacheLessons = lessons
        return lessons
    }
}