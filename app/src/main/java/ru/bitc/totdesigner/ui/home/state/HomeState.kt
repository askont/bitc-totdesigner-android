package ru.bitc.totdesigner.ui.home.state

import ru.bitc.totdesigner.platfom.adapter.state.HomeLessonItem
import ru.bitc.totdesigner.platfom.state.State

data class HomeState(
    val state: State,
    val scrollToStart: Boolean,
    val lessonItems: List<HomeLessonItem>
)