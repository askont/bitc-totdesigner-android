package ru.bitc.totdesigner.ui.home.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.SavedLesson
import ru.bitc.totdesigner.model.interactor.HomeLessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.home.dialog.state.DetailedLessonState
import ru.terrakok.cicerone.Router

/**
 * Created on 06.03.2020
 * @author YWeber */

class DetailedLessonViewModel(
    private val remotePath: String,
    private val useCase: HomeLessonUseCase,
    private val downloadNotifier: DownloadNotifier,
    private val resourceManager: ResourceManager,
    private val router: Router
) : BaseViewModel() {

    private val action = MutableLiveData<DetailedLessonState>()

    private val currentState
        get() = action.value ?: DetailedLessonState("", State.Loading, listOf())

    val viewState: LiveData<DetailedLessonState>
        get() = action

    init {
        launch {
            useCase.getSavedLesson(remotePath)
                .onStart { updateState(State.Loading) }
                .catch { updateState(State.Error(resourceManager.getString(R.string.error_user_message))) }
                .onCompletion { updateState(State.Loaded) }
                .collect {
                    val createItems = createItems(it)
                    action.value = currentState.copy(remotePath = it.remotePath, detailedItems = createItems)
                }
        }
    }

    private fun updateState(newState: State) {
        action.value = currentState.copy(state = newState)
    }

    private fun createItems(savedLesson: SavedLesson): List<DetailedLessonItem> {
        val images = savedLesson.otherImage.map { Preview(it) }
        val mainPreview = Preview(savedLesson.mainImage)

        val allImage = mutableListOf<Preview>()
        allImage.add(mainPreview.copy(isSelect = true))
        allImage.addAll(images)

        val galleryPreview = GalleryPreview(allImage)
        val title = TitleDetailedItem(savedLesson.nameLesson)
        val description = DescriptionDetailedItem(savedLesson.descriptionLesson)
        return listOf(mainPreview, galleryPreview, title, description)
    }

    fun deleteLesson() {
        useCase.deleteSaveLesson()
        downloadNotifier.updateLessonsPreview(true)
        back()
    }

    fun runInteractive() {
        router.navigateTo(AppScreens.InteractionRootScreen(currentState.remotePath))
        back()
    }

    fun back() {
        action.value = currentState.copy(exit = true)
    }

    fun selectNewPreview(image: Preview) {
        val oldItem = currentState.detailedItems
            .filterIsInstance<Preview>()
            .firstOrNull() ?: return
        val mutableItems = currentState.detailedItems.toMutableList()
        mutableItems.remove(oldItem)
        mutableItems.add(0, oldItem.copy(path = image.path))
        action.value = currentState.copy(detailedItems = mutableItems)
    }

}