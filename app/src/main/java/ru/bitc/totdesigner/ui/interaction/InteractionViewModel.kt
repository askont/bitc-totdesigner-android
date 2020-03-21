package ru.bitc.totdesigner.ui.interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.entity.interaction.Scene
import ru.bitc.totdesigner.model.interactor.StartInteractionUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.ui.interaction.state.ImageParticle
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.bitc.totdesigner.ui.interaction.state.SceneState

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionViewModel(
    private val lessonPath: String,
    private val useCase: StartInteractionUseCase
) : BaseViewModel() {

    private val action = MutableLiveData<InteractionState>()

    private val currentState
        get() = action.value ?: InteractionState(
            SceneState(
                false,
                0,
                "",
                listOf()
            ), listOf()
        )
    private var scenesState: MutableList<SceneState> = mutableListOf()
    val viewState: LiveData<InteractionState>
        get() = action

    init {
        launch {
            useCase.getStartLesson(lessonPath)
                .collect {
                    updateState(it)
                }
        }
    }

    private fun updateState(interaction: Interaction) {
        val sceneState = interaction.scenes.map { scene ->
            SceneState(
                scene.partImages.isEmpty(),
                scene.position,
                scene.description,
                scene.partImages.filter { !it.isStatic }.map { InteractionPartItem.Part(it.pathImage, it.namePart) },
                createParticle(scene),
                scene.partImages.isNotEmpty()
            )

        }
        scenesState.addAll(sceneState)
        val previewList = interaction.scenes
            .map { InteractionPartItem.Preview(it.previewImagePath, it.position, it.position == 0) }
        action.value = currentState.copy(sceneState = sceneState[0], previewImages = previewList)
    }

    private fun createParticle(scene: Scene) =
        scene.partImages.map {
            ImageParticle(
                it.pathImage,
                it.positionX,
                it.positionY,
                it.height,
                it.width
            )
        }

    fun selectImage(interactionItem: InteractionPartItem) {

        when (interactionItem) {
            is InteractionPartItem.Preview -> {
                val oldSceneState = currentState.sceneState
                if (oldSceneState.position == interactionItem.position) return
                val newPreview = currentState.previewImages.map {
                    if (it.path == interactionItem.path) {
                        it.copy(isSelect = true)
                    } else {
                        it.copy(isSelect = false)
                    }
                }
                action.value =
                    currentState.copy(sceneState = scenesState[interactionItem.position], previewImages = newPreview)
                scenesState.removeAt(oldSceneState.position)
                scenesState.add(oldSceneState.position, oldSceneState)
            }
        }
    }

    fun switchSide() {
        action.value = currentState.copy(
            sceneState = currentState.sceneState
                .copy(visibleDescription = !currentState.sceneState.visibleDescription)
        )
    }

}