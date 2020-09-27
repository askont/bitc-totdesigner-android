package ru.bitc.totdesigner.ui.interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.interactor.StartInteractionUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.platfom.mapper.SceneStateMapper
import ru.bitc.totdesigner.system.StringUuidBuilder
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
    private val stateMapper: SceneStateMapper,
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    private val action = MutableLiveData<InteractionState>()

    private val currentState
        get() = action.value ?: stateMapper.defaultInteractionState

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
        val sceneState = interaction.scenes.map { scene -> stateMapper.mapToSceneState(scene) }
        scenesState.addAll(sceneState)
        allParticleList.addAll(sceneState.map { it.imageParticle }.flatten())
        val previewList = stateMapper.mapToPartPreviewItem(interaction.scenes)
        updateInteractionState(sceneState = sceneState[0], previewImages = previewList)
    }

    fun selectImage(interactionItem: InteractionPartItem) {
        when (interactionItem) {
            is InteractionPartItem.Preview -> {
                val oldSceneState = currentState.sceneState
                if (oldSceneState.position == interactionItem.position) return
                val newPreview = currentState.previewImages.map { it.copy(isSelect = it.path == interactionItem.path) }
                updateInteractionState(
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
        updateInteractionState(
            sceneState = currentState.sceneState.copy(
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
        updateInteractionState(
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
            updateInteractionState(
                sceneState = startSceneState.copy(
                    imageParticle = correctParticle,
                    isRunPlay = true,
                    changeParticle = true,
                    partImages = currentState.sceneState.partImages.map { it.copy(isPermissionDrop = true) }
                ))
        } else {
            val oldScene = oldSceneState.first { it.position == startSceneState.position }
            updateInteractionState(
                sceneState = oldScene.copy(
                    isRunPlay = false,
                    changeParticle = true,
                    partImages = oldScene.partImages.map { it.copy(isPermissionDrop = false) })
            )
            oldSceneState.remove(oldScene)
        }
    }

    fun handleDragParticle(id: String, newX: Int, newY: Int) {
        val (partId, partX, partY) = uuidBuilder.sliceCombineUuid(id)
        val particleCandidate = allParticleList.filter { it.id == partId }.map {
            val centerX = (it.positionX + it.height) / 2
            val centerY = (it.positionY + it.width) / 2
            it to sqrt((newX - centerX).toDouble().pow(2.0) + (newY - centerY).toDouble().pow(2.0))
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
        val buildPartId = uuidBuilder.buildPartId(particle.id, particle.positionX.toString(), particle.positionY.toString())

        val doneImageParticle = currentState.sceneState.mapDoneScene.toMutableMap()
        if (correctParticle.isSuccessArea) {
            if (doneImageParticle[buildPartId] == false) {
                doneImageParticle[buildPartId] = true
            }
        } else {
            if (doneImageParticle[buildPartId] == true) {
                doneImageParticle[buildPartId] = false
            }
        }

        particleList.removeAll { it.id == partId && it.positionY.toString() == partY && it.positionX.toString() == partX }

        particleList.add(correctParticle)
        val isDoneInteractive = doneImageParticle.map { it.value }.reduce { acc, value -> acc && value }
        updateInteractionState(
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

    private fun updateInteractionState(
        sceneState: SceneState = currentState.sceneState,
        previewImages: List<InteractionPartItem.Preview> = currentState.previewImages
    ) {
        action.value = currentState.copy(sceneState = sceneState, previewImages = previewImages)
    }

    fun markDeleteParticle(particle: ImageParticle) {
        if (particle.isSuccessArea) return
        val listParticle = currentState.sceneState.imageParticle.map {
            if (particle.id == it.id && particle.positionX == it.positionX && particle.positionY == it.positionY) it.copy(
                isDeleteCandidate = !particle.isDeleteCandidate
            ) else it
        }
        val newState = currentState.sceneState.copy(imageParticle = listParticle)
        updateInteractionState(sceneState = newState)
    }

    fun deleteAllMarkParticle() {
        val newState =
            currentState.sceneState.copy(imageParticle = currentState.sceneState.imageParticle.filter { !it.isDeleteCandidate })
        updateInteractionState(sceneState = newState)
    }

}