package ru.bitc.totdesigner.ui.home.state

import ru.bitc.totdesigner.platfom.adapter.state.LessonItem
import ru.bitc.totdesigner.platfom.state.State

data class HomeState(
    val state: State,
    val title: String,
    val description: String,
    val lessonItems: List<LessonItem>
)