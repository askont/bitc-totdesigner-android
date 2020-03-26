package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.entity.interaction.Scene
import ru.bitc.totdesigner.model.repository.StartInteractionRepository
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.notifier.WindowsSizeNotifier
import ru.bitc.totdesigner.system.printDebug

/**
 * Created on 12.03.2020
 * @author YWeber */

class StartInteractionUseCase(
    private val repository: StartInteractionRepository,
    private val offsetWindowsSize: WindowsSizeNotifier,
    private val dispatcher: DispatcherProvider
) {
    suspend fun getStartLesson(lessonPath: String):Flow<Interaction> =
        repository.getStartLesson(lessonPath)
            .combineTransform(offsetWindowsSize.sizeChanges().take(1)) { interaction, size ->
                val offsetWidth = (1920F / size.width)
                val offsetHeight = (1080F / size.height).printDebug()
                val normalizerCoordinateImage = interaction.scenes
                    .map { scene -> platformCoordinate(scene, offsetWidth, offsetHeight) }
                emit(interaction.copy(scenes = normalizerCoordinateImage))
            }.map { interaction ->
                interaction.copy(scenes = interaction.scenes.sortedBy { it.position })
            }.flowOn(dispatcher.default)

    private fun platformCoordinate(
        scene: Scene,
        offsetWidth: Float,
        offsetHeight: Float
    ): Scene {
        return scene.copy(partImages = scene.partImages
            .map {
                it.copy(
                    positionX = (it.positionX / offsetWidth).toInt(),
                    positionY = (it.positionY / offsetHeight).toInt(),
                    height = (it.height / offsetHeight).toInt(),
                    width = (it.width / offsetWidth).toInt()
                )
            })
    }
}