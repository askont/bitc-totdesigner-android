package ru.bitc.totdesigner.ui.catalog.dialog.state

/**
 * Created on 26.01.2020
 * @author YWeber */

sealed class DownloadViewState {
    data class Free(
        val nameQuest: String,
        val description: String,
        val url: String
    ) : DownloadViewState()

    data class Paid(
        val nameQuest: String,
        val description: String,
        val url: String
    ) : DownloadViewState()
}