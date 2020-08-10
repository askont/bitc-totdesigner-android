package ru.bitc.totdesigner.platfom.adapter.state

import androidx.annotation.DrawableRes

/**
 * Created on 05.08.2020
 * @author YWeber */

data class SettingItem(
    @DrawableRes val drawableRes: Int,
    val title: String,
    val checked: Boolean
)