package ru.bitc.totdesigner.ui.interaction.state

import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem

/**
 * Created on 11.03.2020
 * @author YWeber */

data class InteractionState(
    val sceneState: SceneState,
    val previewImages: List<InteractionPartItem.Preview>
)

data class SceneState(
    val visibleDescription: Boolean,
    val position: Int,
    val description: String,
    val partImages: List<InteractionPartItem.Part>,
    val imageParticle: List<ImageParticle> = listOf(),
    val disableSwitch: Boolean = false
)

data class ImageParticle(
    val path: String,
    val positionX: Int = 0,
    val positionY: Int = 0,
    val height: Int = 0,
    val width: Int = 0
)