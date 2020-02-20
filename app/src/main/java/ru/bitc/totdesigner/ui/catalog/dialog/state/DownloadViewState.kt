package ru.bitc.totdesigner.ui.catalog.dialog.state

/**
 * Created on 26.01.2020
 * @author YWeber */

sealed class DownloadViewState(open val lessonUrl: String, open val nameLesson: String) {
    data class Free(
        override val nameLesson: String,
        val description: String,
        val url: String,
        override val lessonUrl: String
    ) : DownloadViewState(lessonUrl, nameLesson)

    data class Paid(
        override val nameLesson: String,
        val description: String,
        val url: String,
        override val lessonUrl: String
    ) : DownloadViewState(lessonUrl, nameLesson)
}