package ru.bitc.totdesigner.ui.home.state

import ru.bitc.totdesigner.platfom.adapter.state.QuestItem
import ru.bitc.totdesigner.platfom.state.State

data class HomeState(
    val state: State,
    val title: String,
    val description: String,
    val questItems: List<QuestItem>
)