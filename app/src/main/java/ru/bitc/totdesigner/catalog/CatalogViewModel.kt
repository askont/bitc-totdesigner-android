package ru.bitc.totdesigner.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.catalog.state.CatalogState
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.iteractor.LessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager

/*
 * Created on 2019-12-06
 * @author YWeber
 */
class CatalogViewModel(
    private val resourceManager: ResourceManager,
    private val useCase: LessonUseCase
) : BaseViewModel() {

    private val action = MutableLiveData<CatalogState>()

    private val currentState
        get() = action.value ?: defaultCatalogData()

    val viewState: LiveData<CatalogState>
        get() = action

    private fun defaultCatalogData(): CatalogState {
        val title = resourceManager.getString(R.string.title_catalog)
        val description = resourceManager.getString(R.string.description_catalog)
        return CatalogState(State.Loaded, title, description, listOf(), false)
    }

    fun updateState() {
        launch {
            useCase.getLessonPreview(::handleState, ::handleLesson)
        }
    }

    override fun handleState(state: State) {
        super.handleState(state)
        currentState.copy(state = state)
    }

    private fun handleLesson(previewLessons: PreviewLessons) {
        val fullItems = mutableListOf<QuestItem>()
        fullItems.addAll(addPaidItem(previewLessons.previews))
        fullItems.addAll(addFreeItem(previewLessons.previews))
        fullItems.add(ButtonQuestItem(""))
        action.value = currentState.copy(questItems = fullItems)

    }

    private fun addFreeItem(lessons: List<PreviewLessons.Lesson>): List<QuestItem> {
        val items = lessons
            .filter { it.category == PreviewLessons.Category.FREE }
            .map { FreeCardQuestItem(it.title, it.imageUrl) }
        val currentItemsMutable = mutableListOf<QuestItem>()
        currentItemsMutable.add(TitleQuestItem(resourceManager.getString(R.string.title_free_quest_item)))
        currentItemsMutable.addAll(items)
        return currentItemsMutable.toList()
    }

    private fun addPaidItem(lessons: List<PreviewLessons.Lesson>): List<QuestItem> {
        val items = lessons
            .filter { it.category == PreviewLessons.Category.PAID }
            .map { PaidCardQuestItem(it.title, it.imageUrl) }
        val currentItemsMutable = mutableListOf<QuestItem>()
        currentItemsMutable.add(TitleQuestItem(resourceManager.getString(R.string.title_paid_quest_item)))
        currentItemsMutable.addAll(items)
        return currentItemsMutable.toList()
    }

    fun eventClick(questItem: QuestItem) {
        when (questItem) {
            is ButtonQuestItem -> {
                action.value = currentState.copy(scrollToStart = true)
            }
        }
    }

}