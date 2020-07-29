package ru.bitc.totdesigner.ui.interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.entity.interaction.PartImage
import ru.bitc.totdesigner.model.entity.interaction.Scene
import ru.bitc.totdesigner.model.interactor.StartInteractionUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.system.printDebug
import ru.bitc.totdesigner.ui.interaction.state.ImageParticle
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.bitc.totdesigner.ui.interaction.state.SceneState
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionViewModel(
    private val lessonPath: String,
    private val useCase: StartInteractionUseCase,
    private val router: Router,
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    private val action = MutableLiveData<InteractionState>()

    private val currentState
        get() = action.value ?: InteractionState(
            SceneState(
                false,
                0,
                "",
                listOf(),
                countSuccess = 0
            ), listOf()
        )
    private var scenesState: MutableList<SceneState> = mutableListOf()
    private val oldSceneState: MutableList<SceneState> = mutableListOf()
    private val allParticleList: MutableList<ImageParticle> = mutableListOf()
    val viewState: LiveData<InteractionState>
        get() = action

    init {
        launch {
            useCase.getStartLesson(lessonPath)
                .collect { startUpdateState(it) }
        }
    }

    private fun startUpdateState(interaction: Interaction) {
        val sceneState = interaction.scenes.map { scene ->
            val notClickablePart = scene.partImages.any { !it.isStatic }
            val partImageInteractive = scene.partImages.filter { !it.isStatic }
            SceneState(
                !notClickablePart,
                scene.position,
                scene.description,
                partImageInteractive.map {
                    InteractionPartItem.Part(
                        createViewId(it, scene.position.toString()),
                        it.pathImage,
                        it.namePart,
                        it.positionX,
                        it.positionY,
                        it.height,
                        it.width,
                        false
                    )
                }.shuffled(),
                createParticle(scene),
                notClickablePart,
                changeParticle = true,
                countSuccess = partImageInteractive.size
            )

        }
        scenesState.addAll(sceneState)
        allParticleList.addAll(sceneState.map { it.imageParticle }.flatten())
        val previewList = interaction.scenes
            .map { InteractionPartItem.Preview(it.previewImagePath, it.position, it.position == 0) }
        action.value = currentState.copy(sceneState = sceneState[0], previewImages = previewList)
    }

    private fun createViewId(it: PartImage, scene: String) =
        it.pathImage + "[$scene]" + it.positionX + it.positionY

    private fun createParticle(scene: Scene) =
        scene.partImages.map {
            ImageParticle(
                createViewId(it, scene.position.toString()),
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
                    currentState.copy(
                        sceneState = scenesState[interactionItem.position].copy(
                            changeParticle = true,
                            partImages = scenesState[interactionItem.position].partImages.shuffled()
                        ),
                        previewImages = newPreview
                    )
                scenesState.removeAt(oldSceneState.position)
                scenesState.add(oldSceneState.position, oldSceneState)
            }
        }
    }

    fun switchSide() {
        action.value = currentState.copy(
            sceneState = currentState.sceneState
                .copy(
                    visibleDescription = !currentState.sceneState.visibleDescription,
                    changeParticle = false
                )
        )
    }

    fun back() {
        router.exit()
    }

    fun nextScene() {
        val oldPosition = currentState.sceneState.position
        val oldScene = currentState.sceneState
        val newPosition = if (oldPosition + 1 in 0 until scenesState.size) oldPosition + 1 else 0
        val newPreview = currentState.previewImages.map {
            if (it.position == newPosition) {
                it.copy(isSelect = true)
            } else {
                it.copy(isSelect = false)
            }
        }
        scenesState.remove(oldScene)
        scenesState.add(oldPosition, oldScene)
        action.value =
            currentState.copy(
                sceneState = scenesState[newPosition].copy(
                    changeParticle = true,
                    partImages = scenesState[newPosition].partImages.shuffled()
                ),
                previewImages = newPreview
            )
    }

    fun playOrStopInteractive() {
        val startSceneState = currentState.sceneState
        if (!currentState.sceneState.isRunPlay) {
            val correctParticle = startSceneState.imageParticle.filter { it.isStatic }
            oldSceneState.add(startSceneState)
            action.value =
                currentState.copy(
                    sceneState = startSceneState.copy(
                        imageParticle = correctParticle,
                        isRunPlay = true,
                        changeParticle = true,
                        partImages = currentState.sceneState.partImages.map { it.copy(isPermissionDrop = true) }
                    )
                )
        } else {
            val oldScene = oldSceneState.first { it.position == startSceneState.position }
            action.value = currentState.copy(
                sceneState = oldScene.copy(
                    isRunPlay = false,
                    changeParticle = true,
                    partImages = oldScene.partImages.map { it.copy(isPermissionDrop = false) })
            )
            oldSceneState.remove(oldScene)
        }
    }

    fun handleDragParticle(id: String, newX: Int, newY: Int) {
        val scene = scenesState[currentState.sceneState.position]
        val particle = allParticleList.find { it.id.printDebug("pre") == id.printDebug("post") } ?: return
        val newPositionParticle = particle.copy(
            positionY = newY - particle.height / 2,
            positionX = newX - particle.width / 2,
            isMoveAnimate = false
        )
        val correctParticle = isParticleInWorkArea(newPositionParticle, particle)
        val particleList = currentState.sceneState.imageParticle.map { it.copy(isAddAnimate = false) }.toMutableList()
        val partList = currentState.sceneState.partImages.map {
            if (it.id == id && correctParticle.isSuccessArea) {
                it.copy(isPermissionDrop = false)
            } else if (it.id == id && !correctParticle.isSuccessArea) {
                it.copy(isPermissionDrop = true)
            } else {
                it
            }
        }
        particleList.removeAll { it.id == id }
        particleList.add(correctParticle)

        val isDoneInteractive =
            scene.countSuccess == particleList.filter { it.isSuccessArea && !it.isStatic }.size &&
                    currentState.sceneState.partImages.filter { it.isPermissionDrop }.size <= 1

        action.value = currentState.copy(
            sceneState = currentState.sceneState.copy(
                imageParticle = particleList,
                partImages = if (isDoneInteractive) listOf() else partList,
                isDoneInteractive = isDoneInteractive
            )
        )

    }

    private fun isParticleInWorkArea(newParticle: ImageParticle, successParticle: ImageParticle): ImageParticle {
        return if (areaChecker(newParticle, successParticle)
        ) {
            successParticle.copy(isMoveAnimate = false, isSuccessArea = true, isAddAnimate = true)
        } else newParticle.copy(isSuccessArea = false, isAddAnimate = false)
    }

    private fun areaChecker(
        newParticle: ImageParticle,
        successParticle: ImageParticle,
        delta: Int = 20
    ) =
        ((newParticle.positionX + delta in successParticle.positionX..(successParticle.positionX + successParticle.width) ||
                newParticle.positionX - delta in successParticle.positionX..(successParticle.positionX + successParticle.width)) &&
                ((newParticle.positionY + delta in successParticle.positionY..(successParticle.positionY + successParticle.height)) ||
                        newParticle.positionY - delta in successParticle.positionY..(successParticle.positionY + successParticle.height)))

}