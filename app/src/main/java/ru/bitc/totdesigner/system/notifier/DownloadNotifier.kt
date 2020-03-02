package ru.bitc.totdesigner.system.notifier

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow

/**
 * Created on 26.01.2020
 * @author YWeber
 * */
class DownloadNotifier {

    private val action = BroadcastChannel<Event>(10)
    private val actionAllVisible = BroadcastChannel<Boolean>(10)

    fun subscribeStatus() = action.asFlow()

    fun eventStatus(lessonUrl: String, lessonName: String, isDelete: Boolean = false) {
        action.offer(Event(lessonUrl, lessonName, isDelete))
        eventVisible(!isDelete)
    }

    fun subscribeAllVisible() = actionAllVisible.asFlow()

    fun eventVisible(visible: Boolean) {
        actionAllVisible.offer(visible)
    }

    data class Event(
        val lessonUrl: String,
        val lessonName: String,
        val isDelete: Boolean
    )

}