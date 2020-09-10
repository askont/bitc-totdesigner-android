package ru.bitc.totdesigner.model.repository

import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.model.models.Lessons
import ru.bitc.totdesigner.model.prefs.PrefsStore
import ru.bitc.totdesigner.platfom.converter.ModelLessonToEntityPreviewConverter
import ru.bitc.totdesigner.system.printDebug

/*
 * Created on 2019-12-19
 * @author YWeber
 */
class LessonRepository(
        private val api: SoapApi,
        private val toEntityPreviewConverter: ModelLessonToEntityPreviewConverter,
        private val pathDao: PathDao,
        private val prefsStore: PrefsStore
) {

    private var cacheLessons: Lessons? = null

    val currentBackground
        get() = prefsStore.currentBackground

    suspend fun getFilterLocalPreviewLessons(): PreviewLessons {
        val lessons = getLessonsCacheOrRemote()
        return toEntityPreviewConverter.convertModelToEntity(lessons)
    }

    suspend fun getAllRemoteLesson(): PreviewLessons {
        val lessons = getLessonsCacheOrRemote()
        return toEntityPreviewConverter.convertModelToEntity(lessons)
    }

    suspend fun getRemoteLessonUrlByName(lessonName: String): String {
        val lessonPath = pathDao.findLessonByName(lessonName).printDebug()
        return lessonPath?.lessonRemoteUrl ?: ""
    }

    private suspend fun getLessonsCacheOrRemote(): Lessons {
        val lessons = cacheLessons ?: api.getLessonsPreview()
        cacheLessons = lessons
        return lessons
    }

    fun saveBackgroundPrefs(background: Int) {
        prefsStore.currentBackground = background
    }
}