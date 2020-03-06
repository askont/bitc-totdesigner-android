package ru.bitc.totdesigner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.SavedLesson
import ru.bitc.totdesigner.model.interactor.HomeLessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.home.state.HomeState
import ru.terrakok.cicerone.Router

class HomeViewModel(
    private val resourceManager: ResourceManager,
    private val homeUseCase: HomeLessonUseCase,
    private val downloadNotifier: DownloadNotifier,
    private val router: Router
) : BaseViewModel() {

    private val action = MutableLiveData<HomeState>()

    private val currentState
        get() = action.value ?: HomeState(
            State.Loaded,
            scrollToStart = false,
            lessonItems = listOf()
        )

    val viewState: LiveData<HomeState>
        get() = action

    private var job: Job? = null

    init {
        action.value = currentState
        job = startRenderItem()
        launch {
            downloadNotifier.subscribeChangePreviewList()
                .collect {
                    if (it) {
                        job?.cancel()
                        job = startRenderItem()
                    }
                }
        }
    }

    fun eventClick(item: HomeLessonItem) {
        when (item) {
            is BottomHomeLessonItem -> {
                action.value = currentState.copy(scrollToStart = true)
            }
            is SavedHomeLessonItem -> {
                router.navigateTo(MainScreens.DetailedLessonDialogScreen(item.lessonUrl))
            }
        }
    }

    private fun startRenderItem(): Job {
        return launch {
            homeUseCase.getAllSavedLessons()
                .onEach { updateItems(it) }
                .launchIn(viewModelScope)
        }
    }

    private fun updateItems(listSaved: List<SavedLesson>) {
        val oldItems = mutableListOf<HomeLessonItem>()
        oldItems.add(defaultHomeData())
        val items = listSaved
            .map { SavedHomeLessonItem(it.remotePath, it.nameLesson, it.mainImage) }
        if (items.isNotEmpty()) {
            oldItems.add(TitleHomeLessonItem(resourceManager.getString(R.string.title_home)))
        }
        oldItems.addAll(items)
        if (items.size > 6) {
            oldItems.add(BottomHomeLessonItem(resourceManager.getString(R.string.btn_text_scroll)))
        }
        action.value = currentState.copy(lessonItems = oldItems, scrollToStart = false)
    }

    private fun defaultHomeData(): HeaderHomeLesson {
        val title = resourceManager.getString(R.string.title_home)
        val description = resourceManager.getString(R.string.description_home)
        return HeaderHomeLesson(title, description)
    }
}