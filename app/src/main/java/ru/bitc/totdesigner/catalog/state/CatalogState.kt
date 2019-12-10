package ru.bitc.totdesigner.catalog.state

import ru.bitc.totdesigner.platfom.adapter.state.QuestState
import ru.bitc.totdesigner.platfom.state.State

/*
 * Created on 2019-12-08
 * @author YWeber
 */

data class CatalogState(
    val state: State,
    val title: String,
    val description: String,
    val questItems: List<QuestState>
)