package ru.bitc.totdesigner.ui.main.state

/**
 * Created on 27.01.2020
 * @author YWeber */

data class MainState(
    val messageLoading: String,
    val durationProgress: Int,
    val finishLoading: Boolean,
    val visibleLoading: Boolean,
    val isError: Boolean
) {
    fun visibleLoadingHolder() = finishLoading && visibleLoading

    companion object {
        val DEFAULT = MainState(
            "", 0,
            finishLoading = false,
            visibleLoading = false,
            isError = false
        )
    }
}