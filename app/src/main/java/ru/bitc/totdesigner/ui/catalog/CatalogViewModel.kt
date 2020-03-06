package ru.bitc.totdesigner.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.interactor.LessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.catalog.state.CatalogState
import ru.terrakok.cicerone.Router

/*
 * Created on 2019-12-06
 * @author YWeber
 */
class CatalogViewModel(
    private val mainRouter: Router,
    private val resourceManager: ResourceManager,
    private val useCase: LessonUseCase,
    private val downloadNotifier: DownloadNotifier
) : BaseViewModel() {

    private val action = MutableLiveData<CatalogState>()
    private var searchJob: Job? = null
    private val currentState
        get() = action.value ?: defaultCatalogData()
    val viewState: LiveData<CatalogState>
        get() = action

    private fun defaultCatalogData(): CatalogState {
        return CatalogState(State.Loaded, listOf(), scrollToStart = false)
    }

    init {
        updateState()
        launch {
            downloadNotifier.subscribeChangePreviewList()
                .onEach { if (it) updateState() }
                .launchIn(viewModelScope)
        }
    }

    private fun updateState() {
        launch {
            useCase.getLessonPreview(::handleState, ::handleLesson)
        }
    }

    fun search(nameQuest: String) {
        searchJob?.cancel()
        action.value = currentState.copy(
            lastSearchQuest = nameQuest
        )
        searchJob = launch {
            useCase.searchLessons(nameQuest, ::handleState, ::handleLesson)
        }
    }

    override fun handleState(state: State) {
        super.handleState(state)
        currentState.copy(state = state)
    }

    private fun handleLesson(previewLessons: PreviewLessons) {
        val fullItems = mutableListOf<LessonItem>()
        fullItems.addAll(addPaidItem(previewLessons.previews))
        fullItems.addAll(addFreeItem(previewLessons.previews))
        if (fullItems.size > MIN_SIZE_ITEM) {
            fullItems.add(ButtonLessonItem(""))
        }
        if (fullItems.isNotEmpty()) {
            val title = resourceManager.getString(R.string.title_catalog)
            val description = resourceManager.getString(R.string.description_catalog)
            fullItems.add(0, HeaderItem(title, description, ""))
        }
        action.value = currentState.copy(
            lessonItems = fullItems,
            questItemEmpty = fullItems.isNotEmpty()
        )
    }

    private fun addPaidItem(lessons: List<PreviewLessons.Lesson>): List<LessonItem> {
        val items = lessons
            .filter { it.category == PreviewLessons.Category.PAID }
            .map { PaidCardLessonItem(it.title, it.imageUrl) }
        val currentItemsMutable = mutableListOf<LessonItem>()
        if (items.isNotEmpty()) {
            currentItemsMutable.add(TitleLessonItem(resourceManager.getString(R.string.title_paid_quest_item)))
        }
        currentItemsMutable.addAll(items)
        return currentItemsMutable.toList()
    }

    private fun addFreeItem(lessons: List<PreviewLessons.Lesson>): List<LessonItem> {
        val items = lessons
            .filter { it.category == PreviewLessons.Category.FREE }
            .map { FreeCardLessonItem(it.title, it.imageUrl) }
        val currentItemsMutable = mutableListOf<LessonItem>()
        if (items.isNotEmpty()) {
            currentItemsMutable.add(TitleLessonItem(resourceManager.getString(R.string.title_free_quest_item)))
        }
        currentItemsMutable.addAll(items)
        return currentItemsMutable.toList()
    }

    fun eventClick(lessonItem: LessonItem) {
        when (lessonItem) {
            is ButtonLessonItem -> {
                action.value = currentState.copy(scrollToStart = true)
            }
            is FreeCardLessonItem -> {
                action.value = currentState.copy(scrollToStart = false)
                mainRouter.navigateTo(MainScreens.FreeDownloadDialogScreen(lessonItem.name))
            }
            is PaidCardLessonItem -> {
                action.value = currentState.copy(scrollToStart = false)
                mainRouter.navigateTo(MainScreens.FreeDownloadDialogScreen(lessonItem.name))
            }
            is HeaderItem -> {
                downloadNotifier.eventVisible(false)
                mainRouter.navigateTo(MainScreens.LoadingDetailedScreen)
            }
        }
    }

    companion object {
        private const val MIN_SIZE_ITEM = 5
    }

}