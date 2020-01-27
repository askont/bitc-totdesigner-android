package ru.bitc.totdesigner.system.notifier

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.bitc.totdesigner.system.notifier.model.DownloadStatus

/**
 * Created on 26.01.2020
 * @author YWeber */
class DownloadNotifier {

    private val action = BroadcastChannel<DownloadStatus>(10)

    fun subscribeStatus() = action.asFlow()

    fun eventStatus(newStatus: DownloadStatus) {
        action.offer(newStatus)
    }

}