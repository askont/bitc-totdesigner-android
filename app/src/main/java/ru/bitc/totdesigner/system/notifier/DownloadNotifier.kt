package ru.bitc.totdesigner.system.notifier

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel

/**
 * Created on 26.01.2020
 * @author YWeber */
@ExperimentalCoroutinesApi
class DownloadNotifier {


    private val action = BroadcastChannel<Boolean>(10)

    fun subscribeStatus() = action.openSubscription()

    fun eventStatus(newStatus: Boolean) {
        action.offer(newStatus)
    }

}