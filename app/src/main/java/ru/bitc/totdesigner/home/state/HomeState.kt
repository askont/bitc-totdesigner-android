package ru.bitc.totdesigner.home.state

import ru.bitc.totdesigner.platfom.adapter.state.QuestState
import ru.bitc.totdesigner.platfom.state.State

data class HomeState(
    val state: State,
    val title: String,
    val description: String,
    val listTitle: String,
    val questItems: List<QuestState>
)