package ru.bitc.totdesigner.ui.loading.state

/**
 * Created on 12.02.2020
 * @author YWeber */

data class LoadingDetailedState(val loadingMiniItems: List<LoadingMiniItem>) {
}

data class LoadingMiniItem(
    val urlId: String,
    val title: String,
    val imageUrl: String,
    val progress: Int
)