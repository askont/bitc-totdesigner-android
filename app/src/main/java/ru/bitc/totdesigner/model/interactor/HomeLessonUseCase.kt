package ru.bitc.totdesigner.model.interactor

import ru.bitc.totdesigner.model.repository.HomeLessonRepository

/**
 * Created on 03.03.2020
 * @author YWeber */

class HomeLessonUseCase(private val repository: HomeLessonRepository) {

    fun getAllSavedLessons() = repository.getSaveLesson()

    fun getSavedLesson(remotePath: String) = repository.findLessonByRemotePath(remotePath)
    fun deleteSaveLesson() {
        repository.deleteSaveLesson()
    }
}