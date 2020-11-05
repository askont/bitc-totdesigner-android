package ru.bitc.totdesigner.ui.setting

import ru.bitc.totdesigner.platfom.BaseActionViewModel
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.system.notifier.ChangeBackgroundNotifier
import ru.bitc.totdesigner.ui.setting.state.SettingPlatform
import ru.bitc.totdesigner.ui.setting.state.SettingState

/**
 * Created on 05.08.2020
 * @author YWeber */

class SettingViewModel(
    private val settingPlatform: SettingPlatform,
    private val notifier: ChangeBackgroundNotifier,
) :
    BaseActionViewModel<SettingState>(initState = SettingState(emptyList())) {

    init {
        updateState(
            SettingState(settingPlatform.createBackgroundBlurItems)
        )
    }

    fun newBackground(item: SettingItem) {
        when (item) {
            is SettingItem.BackgroundBlur -> {
                val newItems = currentState.items.map {
                    when (it) {
                        is SettingItem.BackgroundBlur -> it.copy(checked = it.drawableRes == item.drawableRes)
                        is SettingItem.Title -> it
                        is SettingItem.StatusSubscription -> it
                    }
                }
                updateState(currentState.copy(items = newItems))
                notifier.sendEvent(item)
            }
            is SettingItem.Title -> Unit
            else -> Unit
        }


    }

}