package ru.bitc.totdesigner.platfom.adapter.state

import androidx.annotation.DrawableRes

/**
 * Created on 05.08.2020
 * @author YWeber */

sealed class SettingItem {

    data class AvailableSubscriptions(
        val subTitle: String,
        val pricesSubscription: List<PriceSubscription>
    ) : SettingItem()

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

data class PriceSubscription(val title: String, val price: String)

enum class Status {
    ACTIVE,
    NOT_ACTIVE

}
