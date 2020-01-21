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
            PreviewLessons.Lesson(it.name, it.previewIcon,createCategoryType(it.category))
        })
    }

    private fun createCategoryType(category: String): PreviewLessons.Category {
        return when (category) {
            PreviewLessons.Category.FREE.category -> PreviewLessons.Category.FREE
            PreviewLessons.Category.PAID.category -> PreviewLessons.Category.PAID
            else -> PreviewLessons.Category.UNKNOW
        }
    }
}