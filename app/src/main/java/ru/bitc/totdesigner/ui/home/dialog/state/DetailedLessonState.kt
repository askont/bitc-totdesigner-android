package ru.bitc.totdesigner.ui.home.dialog.state

import ru.bitc.totdesigner.platfom.adapter.state.DetailedLessonItem
import ru.bitc.totdesigner.platfom.state.State

/**
 * Created on 06.03.2020
 * @author YWeber */

data class DetailedLessonState(
    val remotePath: String,
    val state: State,
    val detailedItems: List<DetailedLessonItem>,
    val exit: Boolean = false
) {
}