package ru.bitc.totdesigner.ui.catalog.state

import ru.bitc.totdesigner.platfom.adapter.state.LessonItem
import ru.bitc.totdesigner.platfom.state.State

/*
 * Created on 2019-12-08
 * @author YWeber
 */

data class CatalogState(
    val state: State,
    val lessonItems: List<LessonItem>,
    val scrollToStart: Boolean,
    val questItemEmpty: Boolean = false,
    val lastSearchQuest: String = ""
)