package ru.bitc.totdesigner.platfom.adapter.state

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * Created on 05.08.2020
 * @author YWeber */

sealed class SettingItem {

    data class StatusSubscription(
        val title: String,
        val status: Status,
        val statusDescription: String,
        val titleButton: String
    ) : SettingItem()

    data class BackgroundBlur(
        @DrawableRes val drawableRes: Int,
        val title: String,
        val checked: Boolean
    ) : SettingItem()

    data class Title(val title: String, val description: String) : SettingItem()

}
enum class Status {
    ACTIVE,
    NOT_ACTIVE

}
