package ru.bitc.totdesigner.ui.setting

import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.prefs.PrefsStore
import ru.bitc.totdesigner.platfom.BaseActionViewModel
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.ChangeBackgroundNotifier
import ru.bitc.totdesigner.ui.setting.state.SettingState

/**
 * Created on 05.08.2020
 * @author YWeber */

class SettingViewModel(
    resourceManager: ResourceManager,
    private val notifier: ChangeBackgroundNotifier,
    prefsStore: PrefsStore
) :
    BaseActionViewModel<SettingState>(initState = SettingState(emptyList())) {

    init {
        updateState(
            SettingState(
                listOf(
                    SettingItem(
                        R.drawable.img_totdesign,
                        resourceManager.getString(R.string.totdesigner),
                        prefsStore.currentBackground == R.drawable.img_totdesign
                    ),
                    SettingItem(
                        R.drawable.img_world,
                        resourceManager.getString(R.string.home_title),
                        prefsStore.currentBackground == R.drawable.img_world
                    ),
                    SettingItem(
                        R.drawable.img_red_planet,
                        resourceManager.getString(R.string.red_planet_title),
                        prefsStore.currentBackground == R.drawable.img_red_planet
                    ),
                    SettingItem(
                        R.drawable.img_ice,
                        resourceManager.getString(R.string.ice_title),
                        prefsStore.currentBackground == R.drawable.img_ice
                    ),
                    SettingItem(
                        R.drawable.img_business,
                        resourceManager.getString(R.string.business),
                        prefsStore.currentBackground == R.drawable.img_business
                    ),
                    SettingItem(
                        R.drawable.img_office,
                        resourceManager.getString(R.string.office),
                        prefsStore.currentBackground == R.drawable.img_office
                    ),
                    SettingItem(
                        R.drawable.img_yellow_flower,
                        resourceManager.getString(R.string.yellow_flower),
                        prefsStore.currentBackground == R.drawable.img_yellow_flower
                    ),
                    SettingItem(
                        R.drawable.img_blue_flower,
                        resourceManager.getString(R.string.blue_flower),
                        prefsStore.currentBackground == R.drawable.img_blue_flower
                    ),
                    SettingItem(
                        R.drawable.img_spring,
                        resourceManager.getString(R.string.spring),
                        prefsStore.currentBackground == R.drawable.img_spring
                    ),
                    SettingItem(
                        R.drawable.img_summer,
                        resourceManager.getString(R.string.summer),
                        prefsStore.currentBackground == R.drawable.img_summer
                    ),
                    SettingItem(
                        R.drawable.img_autumn,
                        resourceManager.getString(R.string.autumn),
                        prefsStore.currentBackground == R.drawable.img_autumn
                    ),
                    SettingItem(
                        R.drawable.img_black_and_white,
                        resourceManager.getString(R.string.black_and_white),
                        prefsStore.currentBackground == R.drawable.img_black_and_white
                    )
                )
            )
        )
    }

    fun newBackground(item: SettingItem) {
        val newItems = currentState.items.map {
            it.copy(checked = it.drawableRes == item.drawableRes)
        }
        updateState(currentState.copy(items = newItems))
        notifier.sendEvent(item)
    }

}