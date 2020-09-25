package ru.bitc.totdesigner.ui.interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.entity.interaction.Scene
import ru.bitc.totdesigner.model.interactor.StartInteractionUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.system.StringUuidBuilder
import ru.bitc.totdesigner.system.printDebug
import ru.bitc.totdesigner.ui.interaction.state.ImageParticle
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.bitc.totdesigner.ui.interaction.state.SceneState
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionViewModel(
    private val lessonPath: String,
    private val useCase: StartInteractionUseCase,
    private val router: Router,
    private val uuidBuilder: StringUuidBuilder,
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
                countSuccess = 0,
                mapDoneScene = emptyMap()
            ), listOf(),
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
            val particleImage = partImageInteractive.map {
                InteractionPartItem.Part(
                    it.guid,
                    it.pathImage,
                    it.namePart,
                    it.positionX,
                    it.positionY,
                    it.height,
                    it.width,
                    false
                )
            }.shuffled().distinctBy { it.id }
            SceneState(
                visibleDescription = !notClickablePart,
                position = scene.position,
                description = scene.description,
                partImages = particleImage,
                imageParticle = createParticle(scene),
                isContentInteractive = notClickablePart,
                changeParticle = true,
                countSuccess = partImageInteractive.size,
                mapDoneScene =   putImageUuidToDone(createParticle(scene))
            )

        }
        scenesState.addAll(sceneState)
        allParticleList.addAll(sceneState.map { it.imageParticle }.flatten())
        val previewList = interaction.scenes
            .map { InteractionPartItem.Preview(it.previewImagePath, it.position, it.position == 0) }
        action.value = currentState.copy(sceneState = sceneState[0], previewImages = previewList)

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
            else -> Unit
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

    private fun putImageUuidToDone(imageParticle: List<ImageParticle>): Map<String, Boolean> {
        return  imageParticle.filter { !it.isStatic }.map {
            uuidBuilder.buildPartId(
                it.id,
                it.positionX.toString(),
                it.positionY.toString()
            ) to false
        }.toMap()
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
        val split = id.split("@")
        val partId = split[0]
        val partX = split[1]
        val partY = split[2]
        val scene = scenesState[currentState.sceneState.position]
        val particleCandidate = allParticleList.filter {
            it.id == partId
        }.map {
            val centerX = (it.positionX + it.height) / 2
            val centerY = (it.positionY + it.width) / 2
            it to sqrt(
                (newX - centerX).toDouble().pow(2.0) + (newY - centerY).toDouble().pow(2.0)
            )
        }
        val minShortSegment = particleCandidate.map { it.second }.reduce { acc, next -> acc.coerceAtMost(next) }
        val particle = particleCandidate.first { it.second == minShortSegment }.first

        val newPositionParticle = particle.copy(
            positionY = newY - particle.height / 2,
            positionX = newX - particle.width / 2,
            isMoveAnimate = false
        )
        val correctParticle = isParticleInWorkArea(newPositionParticle, particle)
        val particleList = currentState.sceneState.imageParticle.map { it.copy(isAddAnimate = false) }.toMutableList()
        val buildPartId = uuidBuilder.buildPartId(
            particle.id,
            particle.positionX.toString(),
            particle.positionY.toString()
        )
        val doneImageParticle = currentState.sceneState.mapDoneScene.toMutableMap()
        if ( correctParticle.isSuccessArea){
            if (doneImageParticle[buildPartId] == false){
                doneImageParticle[buildPartId] = true
            }
        }else{
            if (doneImageParticle[buildPartId] == true){
                doneImageParticle[buildPartId] = false
            }
        }

        particleList.removeAll { it.id == partId && it.positionY.toString() == partY && it.positionX.toString() == partX }

        particleList.add(correctParticle)
        val isDoneInteractive = doneImageParticle.map { it.value }.reduce {acc, value -> acc && value }
        action.value = currentState.copy(
            sceneState = currentState.sceneState.copy(
                imageParticle = particleList,
                isDoneInteractive = isDoneInteractive,
                mapDoneScene = doneImageParticle
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

    fun markDeleteParticle(particle: ImageParticle) {
        if (particle.isSuccessArea) return
        val listParticle = currentState.sceneState.imageParticle.map {
            if (particle.id == it.id && particle.positionX == it.positionX && particle.positionY == it.positionY) it.copy(
                isDeleteCandidate = !particle.isDeleteCandidate
            ) else it
        }
        val newState = currentState.sceneState.copy(imageParticle = listParticle)
        action.value = currentState.copy(sceneState = newState)
    }

    fun deleteAllMarkParticle() {
        val newState =
            currentState.sceneState.copy(imageParticle = currentState.sceneState.imageParticle.filter { !it.isDeleteCandidate })
        action.value = currentState.copy(sceneState = newState)
    }

}