package ru.bitc.totdesigner.platfom.mapper

import ru.bitc.totdesigner.model.entity.interaction.PartImage
import ru.bitc.totdesigner.model.entity.interaction.Scene
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.system.StringUuidBuilder
import ru.bitc.totdesigner.ui.interaction.state.ImageParticle
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.bitc.totdesigner.ui.interaction.state.SceneState

class SceneStateMapper(private val uuidBuilder: StringUuidBuilder) {

    val defaultInteractionState = InteractionState(
        SceneState(
            false,
            0,
            "",
            listOf(),
            countSuccess = 0,
            mapDoneScene = emptyMap()
        ), listOf(),
    )

    fun mapToPartPreviewItem(scene: List<Scene>) : List<InteractionPartItem.Preview>{
        return scene.map { InteractionPartItem.Preview(it.previewImagePath, it.position, it.position == 0) }
    }

    fun mapToSceneState(scene: Scene): SceneState {
        val notClickablePart = scene.partImages.any { !it.isStatic }
        val partImageInteractive = scene.partImages.filter { !it.isStatic }
        val particleImage = partImageInteractive.map { createPartItem(it) }.shuffled().distinctBy { it.id }
        return SceneState(
            visibleDescription = !notClickablePart,
            position = scene.position,
            description = scene.description,
            partImages = particleImage,
            imageParticle = createParticle(scene),
            isContentInteractive = notClickablePart,
            changeParticle = true,
            countSuccess = partImageInteractive.size,
            mapDoneScene = putImageUuidToDone(createParticle(scene))
        )
    }

    private fun createPartItem(it: PartImage): InteractionPartItem.Part {
        return InteractionPartItem.Part(
            it.guid,
            it.pathImage,
            it.namePart,
            it.positionX,
            it.positionY,
            it.height,
            it.width,
            false
        )
    }

    private fun createParticle(scene: Scene) =
        scene.partImages.map {
            ImageParticle(
                it.guid,
                it.pathImage,
                it.positionX,
                it.positionY,
                it.height,
                it.width,
                it.isStatic,
                isMoveAnimate = true,
                isSuccessArea = true
            )
        }

    private fun putImageUuidToDone(imageParticle: List<ImageParticle>): Map<String, Boolean> {
        return imageParticle.filter { !it.isStatic }.map {
            uuidBuilder.buildPartId(
                it.id,
                it.positionX.toString(),
                it.positionY.toString()
            ) to false
        }.toMap()
    }


}