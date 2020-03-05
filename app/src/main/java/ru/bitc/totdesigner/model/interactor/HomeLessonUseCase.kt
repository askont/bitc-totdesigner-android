package ru.bitc.totdesigner.model.interactor

import ru.bitc.totdesigner.model.repository.HomeLessonRepository

/**
 * Created on 03.03.2020
 * @author YWeber */

class HomeLessonUseCase(private val repository: HomeLessonRepository) {

    fun getSavedLesson() = repository.getSaveLesson()
}