package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.flow.map
import ru.bitc.totdesigner.model.repository.StartInteractionRepository

/**
 * Created on 12.03.2020
 * @author YWeber */

class StartInteractionUseCase(private val repository: StartInteractionRepository) {

    suspend fun getStartLesson(lessonPath: String) =
        repository.getStartLesson(lessonPath).map { interaction ->
            interaction.copy(scenes = interaction.scenes.sortedBy { it.position })
        }

}