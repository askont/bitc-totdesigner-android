package ru.bitc.totdesigner.ui.catalog.dialog.state

/**
 * Created on 26.01.2020
 * @author YWeber */

sealed class DownLoadViewState {
    data class Free(
        val nameQuest: String,
        val description: String,
        val url: String
    ) : DownLoadViewState()

    data class Paid(
        val nameQuest: String,
        val description: String,
        val url: String
    ) : DownLoadViewState()
}