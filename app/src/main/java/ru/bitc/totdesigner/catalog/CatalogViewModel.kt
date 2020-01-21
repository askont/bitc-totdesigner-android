package ru.bitc.totdesigner.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.catalog.state.CatalogState
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.iteractor.LessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.ButtonQuestItem
import ru.bitc.totdesigner.platfom.adapter.state.CardQuestItem
import ru.bitc.totdesigner.platfom.adapter.state.QuestItem
import ru.bitc.totdesigner.platfom.adapter.state.TitleQuestItem
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

    fun updateState(){
        launch {
            useCase.getLessonPreview(::handleState, ::handleLesson)
        }
    }

    override fun handleState(state: State) {
        super.handleState(state)
        currentState.copy(state = state)
    }

    private fun handleLesson(previewLessons: PreviewLessons) {
        val lessons = previewLessons.previews.map { CardQuestItem(it.title, it.imageUrl) }
        val titleAndButtonItem = addItem(lessons)
        action.value = currentState.copy(questItems = titleAndButtonItem)

    }

    private fun addItem(lessons: List<CardQuestItem>): List<QuestItem> {
        val currentItemsMutable = mutableListOf<QuestItem>()
        currentItemsMutable.add(TitleQuestItem(resourceManager.getString(R.string.title_quest_item)))
        currentItemsMutable.addAll(lessons)
        currentItemsMutable.add(ButtonQuestItem(""))
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