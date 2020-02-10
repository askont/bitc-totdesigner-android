package ru.bitc.totdesigner.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.iteractor.LessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.printDebug
import ru.bitc.totdesigner.ui.catalog.state.CatalogState
import ru.terrakok.cicerone.Router

/*
 * Created on 2019-12-06
 * @author YWeber
 */
class CatalogViewModel(
    private val router: Router,
    private val resourceManager: ResourceManager,
    private val useCase: LessonUseCase
) : BaseViewModel() {

    private val action = MutableLiveData<CatalogState>()
    private var searchJob: Job? = null

    private val currentState
        get() = action.value ?: defaultCatalogData()

    val viewState: LiveData<CatalogState>
        get() = action

    private fun defaultCatalogData(): CatalogState {
        return CatalogState(State.Loaded, listOf(), false)
    }

    init {
        updateState()
    }

    private fun updateState() {
        launch {
            useCase.getLessonPreview(::handleState, ::handleLesson)
        }
    }

    fun search(nameQuest: String) {
        searchJob?.cancel()
        action.value = currentState.copy(lastSearchQuest = nameQuest.printDebug())
        searchJob = launch {
            useCase.searchLessons(nameQuest, ::handleState, ::handleLesson)

        }
    }

    override fun handleState(state: State) {
        super.handleState(state)
        currentState.copy(state = state)
    }

    private fun handleLesson(previewLessons: PreviewLessons) {
        val fullItems = mutableListOf<QuestItem>()
        val title = resourceManager.getString(R.string.title_catalog)
        val description = resourceManager.getString(R.string.description_catalog)
        fullItems.add(SearchHeaderItem(title, description))
        fullItems.addAll(addPaidItem(previewLessons.previews))
        fullItems.addAll(addFreeItem(previewLessons.previews))
        if (fullItems.size > MIN_SIZE_ITEM) {
            fullItems.add(ButtonQuestItem(""))
        }
        action.value = currentState.copy(questItems = fullItems,
            questItemEmpty = fullItems.any { it !is SearchHeaderItem })
    }

    private fun addFreeItem(lessons: List<PreviewLessons.Lesson>): List<QuestItem> {
        val items = lessons
            .filter { it.category == PreviewLessons.Category.FREE }
            .map { FreeCardQuestItem(it.title, it.imageUrl) }
        val currentItemsMutable = mutableListOf<QuestItem>()
        if (items.isNotEmpty()) {
            currentItemsMutable.add(TitleQuestItem(resourceManager.getString(R.string.title_free_quest_item)))
        }
        currentItemsMutable.addAll(items)
        return currentItemsMutable.toList()
    }

    private fun addPaidItem(lessons: List<PreviewLessons.Lesson>): List<QuestItem> {
        val items = lessons
            .filter { it.category == PreviewLessons.Category.PAID }
            .map { PaidCardQuestItem(it.title, it.imageUrl) }
        val currentItemsMutable = mutableListOf<QuestItem>()
        if (items.isNotEmpty()) {
            currentItemsMutable.add(TitleQuestItem(resourceManager.getString(R.string.title_paid_quest_item)))
        }
        currentItemsMutable.addAll(items)
        return currentItemsMutable.toList()
    }

    fun eventClick(questItem: QuestItem) {
        when (questItem) {
            is ButtonQuestItem -> {
                action.value = currentState.copy(scrollToStart = true)
            }
            is FreeCardQuestItem -> {
                action.value = currentState.copy(scrollToStart = false)
                router.navigateTo(MainScreens.FreeDownloadDialogScreen(questItem.name))
            }
            is PaidCardQuestItem -> {
                action.value = currentState.copy(scrollToStart = false)
                router.navigateTo(MainScreens.FreeDownloadDialogScreen(questItem.name))
            }
        }
    }

    companion object {
        private const val MIN_SIZE_ITEM = 5
    }

}