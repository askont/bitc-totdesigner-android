package ru.bitc.totdesigner.system.notifier

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.bitc.totdesigner.ui.interaction.state.WindowsSize

/**
 * Created on 21.03.2020
 * @author YWeber */

class WindowsSizeNotifier {
    private val action = ConflatedBroadcastChannel<WindowsSize>()
    fun setNewWindowSize(size: WindowsSize) {
        action.offer(size)
    }

    fun sizeChanges() = action.asFlow()
}