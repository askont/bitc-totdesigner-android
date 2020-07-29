package ru.bitc.totdesigner.platfom.converter

import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.models.Lessons

/**
 * Created on 17.02.2020
 * @author YWeber */

class ModelLessonToEntityPreviewConverter {

    fun convertModelToEntity(model: Lessons): PreviewLessons =
        PreviewLessons(model.lessonsInfo.map { convertModelInfoToLesson(it) })

    fun convertModelInfoToLesson(it: Lessons.LessonInfo) =
        PreviewLessons.Lesson(it.name, it.previewIcon, createCategoryType(it.category), it.lessonUrl)

    private fun createCategoryType(category: String): PreviewLessons.Category {
        return when (category) {
            PreviewLessons.Category.FREE.category -> PreviewLessons.Category.FREE
            PreviewLessons.Category.PAID.category -> PreviewLessons.Category.PAID
            else -> PreviewLessons.Category.UN_KNOW
        }
    }
}