package ru.bitc.totdesigner.ui.setting

import ru.bitc.totdesigner.platfom.BaseActionViewModel
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.platfom.adapter.state.Status
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

    fun handleItemClick(item: SettingItem) {
        when (item) {
            is SettingItem.BackgroundBlur -> handleItemClickBackgroundBlur(item)
            is SettingItem.Title -> Unit
            is SettingItem.StatusSubscription -> handleItemClickStatusSubscription(item)
        }


    }


    private fun handleItemClickBackgroundBlur(item: SettingItem.BackgroundBlur) {
        val newItems = currentState.items.map {
            when (it) {
                is SettingItem.BackgroundBlur -> it.copy(checked = it.drawableRes == item.drawableRes)
                else -> it
            }
        }
        updateState(currentState.copy(items = newItems))
        notifier.sendEvent(item)
    }

    private fun handleItemClickStatusSubscription(item: SettingItem.StatusSubscription) {
        val newStatusSubscription = currentState.items.map {
            if (it is SettingItem.StatusSubscription && it.title == item.title) {
                val newStatus = it.status == Status.NOT_ACTIVE
                it.copy(status = if (newStatus) Status.ACTIVE else Status.NOT_ACTIVE,statusDescription = "Годовая возобновляемая, 12 мес.")
            } else it
        }
        updateState(currentState.copy(items = newStatusSubscription))
    }

}