package ru.bitc.totdesigner.system.notifier

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.bitc.totdesigner.system.notifier.model.FreeDownloadPackage

/**
 * Created on 26.01.2020
 * @author YWeber */
class DownloadNotifier {

    private val action = BroadcastChannel<FreeDownloadPackage>(10)
    private val actionAllVisible = BroadcastChannel<Boolean>(10)

    fun subscribeStatus() = action.asFlow()

    fun eventStatus(newStatus: FreeDownloadPackage) {
        action.offer(newStatus)
        eventVisible(true)
    }

    fun subscribeAllVisible() = actionAllVisible.asFlow()

    fun eventVisible(visible: Boolean) {
        actionAllVisible.offer(visible)
    }

}