package ru.bitc.totdesigner.ui.loading.state

import androidx.recyclerview.widget.DiffUtil

/**
 * Created on 12.02.2020
 * @author YWeber */

sealed class LoadingDetailed(open val urlId: String) {
    data class Loading(
        override val urlId: String,
        val imageUrl: String,
        val title: String,
        val progress: Int = 100
    ) : LoadingDetailed(urlId)

    data class Finish(
        override val urlId: String,
        val imageUrl: String,
        val title: String
    ) : LoadingDetailed(urlId)

    data class Error(
        override val urlId: String,
        val imageUrl: String,
        val title: String
    ) : LoadingDetailed(urlId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoadingDetailed

        if (urlId != other.urlId) return false

        return true
    }

    override fun hashCode(): Int {
        return urlId.hashCode()
    }

}

object DetailedDiff : DiffUtil.ItemCallback<LoadingDetailed>() {
    override fun areItemsTheSame(oldItem: LoadingDetailed, newItem: LoadingDetailed): Boolean =
        oldItem.urlId == newItem.urlId

    override fun areContentsTheSame(oldItem: LoadingDetailed, newItem: LoadingDetailed): Boolean =
        oldItem == newItem
}