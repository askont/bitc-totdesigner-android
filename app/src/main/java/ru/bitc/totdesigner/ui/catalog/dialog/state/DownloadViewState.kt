package ru.bitc.totdesigner.ui.catalog.dialog.state

/**
 * Created on 26.01.2020
 * @author YWeber */

sealed class DownloadViewState(open val lessonUrl: String) {
    data class Free(
        val nameQuest: String,
        val description: String,
        val url: String,
        override val lessonUrl: String
    ) : DownloadViewState(lessonUrl)

    data class Paid(
        val nameQuest: String,
        val description: String,
        val url: String,
        override val lessonUrl: String
    ) : DownloadViewState(lessonUrl)
}