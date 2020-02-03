package ru.bitc.totdesigner.model.repository

import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.model.models.Lessons

/*
 * Created on 2019-12-19
 * @author YWeber
 */
class LessonRepository(private val api: SoapApi) {

    private var cacheLessons: Lessons? = null

    suspend fun getPreviewLessons(): PreviewLessons {
        val lessons = getLessonsCacheOrRemote()
        return PreviewLessons(lessons.lessonsInfo.map {
            PreviewLessons.Lesson(it.name, it.previewIcon, createCategoryType(it.category),it.lessonUrl)
        })
    }

    private suspend fun getLessonsCacheOrRemote(): Lessons {
        return if (cacheLessons == null) {
            val lessonsPreview = api.getLessonsPreview()
            cacheLessons = lessonsPreview
            lessonsPreview
        } else {
            cacheLessons ?: api.getLessonsPreview()
        }
    }

    private fun createCategoryType(category: String): PreviewLessons.Category {
        return when (category) {
            PreviewLessons.Category.FREE.category -> PreviewLessons.Category.FREE
            PreviewLessons.Category.PAID.category -> PreviewLessons.Category.PAID
            else -> PreviewLessons.Category.UN_KNOW
        }
    }
}