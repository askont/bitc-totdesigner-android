package ru.bitc.totdesigner.ui.setting

import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActionViewModel
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.ui.setting.state.SettingState

/**
 * Created on 05.08.2020
 * @author YWeber */

class SettingViewModel : BaseActionViewModel<SettingState>(initState = SettingState(emptyList())) {

    init {
        updateState(
            SettingState(
                listOf(
                    SettingItem(R.drawable.img_totdesign, "TOTDESIGNER", true),
                    SettingItem(R.drawable.img_world, "Home", false),
                    SettingItem(R.drawable.img_red_planet, "Red planet", false),
                    SettingItem(R.drawable.img_ice, "Ice", false),
                    SettingItem(R.drawable.img_business, "Business", false),
                    SettingItem(R.drawable.img_office, "Office", false),
                    SettingItem(R.drawable.img_yellow_flower, "Yellow Flower", false),
                    SettingItem(R.drawable.img_blue_flower, "Blue Flower", false),
                    SettingItem(R.drawable.img_spring, "Spring", false),
                    SettingItem(R.drawable.img_summer, "Summer", false),
                    SettingItem(R.drawable.img_autumn, "Autumn  ", false),
                    SettingItem(R.drawable.img_black_and_white, "Black & White", false)
                )
            )
        )
    }

}