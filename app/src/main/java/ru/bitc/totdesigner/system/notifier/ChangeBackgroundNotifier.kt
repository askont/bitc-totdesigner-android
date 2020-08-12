package ru.bitc.totdesigner.system.notifier

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem

/**
 * Created on 12.08.2020
 * @author YWeber */

class ChangeBackgroundNotifier {
    private val notifier = MutableStateFlow<SettingItem?>(null)

    val action
        get() = notifier.filterIsInstance<SettingItem>()

    fun sendEvent(items: SettingItem) {
        notifier.value = null
        notifier.value = items
    }

}