package ru.bitc.totdesigner.ui.main.state

/**
 * Created on 27.01.2020
 * @author YWeber */

data class MainState(
    val messageLoading: String,
    val durationProgress: Int,
    val finishLoading: Boolean,
    val visibleLoading: Boolean
) {
    fun visibleLoadingHolder() = finishLoading && visibleLoading
}