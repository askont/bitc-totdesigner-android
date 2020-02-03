package ru.bitc.totdesigner.ui.main.state

/**
 * Created on 27.01.2020
 * @author YWeber */

data class LoadingItem(
    val urlId: String,
    val progress: Int, val message: String
)