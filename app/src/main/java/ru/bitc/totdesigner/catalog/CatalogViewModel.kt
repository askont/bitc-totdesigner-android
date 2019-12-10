package ru.bitc.totdesigner.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.catalog.state.CatalogState
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.CardQuestState
import ru.bitc.totdesigner.platfom.adapter.state.QuestState
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager

/*
 * Created on 2019-12-06
 * @author YWeber
 */
class CatalogViewModel(private val resourceManager: ResourceManager) : BaseViewModel() {

    private val action = MutableLiveData<CatalogState>()

    private val currentState
        get() = action.value ?: defaultCatalogData()

    val viewState: LiveData<CatalogState>
        get() = action

    private fun defaultCatalogData(): CatalogState {
        val title = resourceManager.getString(R.string.title_catalog)
        val description = resourceManager.getString(R.string.description_catalog)
       return CatalogState(State.Loaded,title,description, listOf())
    }

    fun updateState(){
        action.value = currentState.copy(questItems = testCardData())
    }

    private fun testCardData() = listOf<QuestState>(
        CardQuestState("Оригами-квест Колибри","https://i.ytimg.com/vi/9q43os6jvM4/maxresdefault.jpg"),
        CardQuestState("Оригами-квест КолибриОригами-квест Лисица","https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Solar_System_size_to_scale_ru.svg/1200px-Solar_System_size_to_scale_ru.svg.png"),
        CardQuestState("Оригами-квест Лисица","https://www.vladtime.ru/uploads/posts/2019-07/1563066764_snimok-ekrana-2019-07-14-v-11_10_14.jpg"),
        CardQuestState("Оригами-квест Хамелеон","https://kipmu.ru/wp-content/uploads/pplzvkr.jpg"),
        CardQuestState("Оригами-квест Медуза","https://img.gazeta.ru/files3/555/10733555/Untitled-1-pic905-895x505-41340.jpg"),
        CardQuestState("Оригами-квест Осьминог","https://img.gazeta.ru/files3/555/10733555/Untitled-1-pic905-895x505-41340.jpg"),
        CardQuestState("Основы проектирования UX UI","https://img.gazeta.ru/files3/555/10733555/Untitled-1-pic905-895x505-41340.jpg"),
        CardQuestState("Планеты солнечной системы","https://img.gazeta.ru/files3/555/10733555/Untitled-1-pic905-895x505-41340.jpg"),
        CardQuestState("Планеты солнечной системы","https://img.gazeta.ru/files3/555/10733555/Untitled-1-pic905-895x505-41340.jpg")
    )

}