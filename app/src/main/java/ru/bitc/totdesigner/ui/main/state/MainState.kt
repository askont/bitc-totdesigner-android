package ru.bitc.totdesigner.ui.main.state

import androidx.annotation.DrawableRes
import ru.bitc.totdesigner.R

/**
 * Created on 27.01.2020
 * @author YWeber */

data class MainState(
    val messageLoading: String,
    val durationProgress: Int,
    val finishLoading: Boolean,
    val visibleLoading: Boolean,
    val isError: Boolean,
    val isBlur: Boolean,
    @DrawableRes
    val background: Int
) {
    fun visibleLoadingHolder() = finishLoading && visibleLoading

    companion object {
        val DEFAULT = MainState(
            "", 0,
            finishLoading = false,
            visibleLoading = false,
            isError = false,
            isBlur = false,
            background = R.drawable.img_totdesign
        )
    }
}