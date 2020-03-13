package ru.bitc.totdesigner.ui.interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.interactor.StartInteractionUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.system.printDebug
import ru.bitc.totdesigner.ui.interaction.state.InteractionState

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionViewModel(
    private val lessonPath: String,
    private val useCase: StartInteractionUseCase
) : BaseViewModel() {

    private val action = MutableLiveData<InteractionState>()

    private val currentState
        get() = action.value ?: InteractionState("", listOf(), listOf())

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
        val mainImage = interaction.previews.first().pathImage
        val partImages = interaction.partImages
            .map { InteractionPartItem.Part(it.pathImage, it.namePart) }
        val preview = interaction.previews
            .map { InteractionPartItem.Preview(it.pathImage, it.position == 0) }
        action.value = currentState.copy(rootPicture = mainImage, partImages = partImages, previewImages = preview)
    }

    fun selectImage(interactionItem: InteractionPartItem) {
        interactionItem.printDebug()
        when (interactionItem) {
            is InteractionPartItem.Preview -> {
                val newMainImage = interactionItem.path
                val newPreview = currentState.previewImages.map {
                    if (it.path == interactionItem.path) {
                        it.copy(isSelect = true)
                    } else {
                        it.copy(isSelect = false)
                    }
                }
                action.value = currentState.copy(rootPicture = newMainImage, previewImages = newPreview)
            }
        }
    }

}