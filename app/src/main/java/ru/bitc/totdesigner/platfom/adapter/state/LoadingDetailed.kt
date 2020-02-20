package ru.bitc.totdesigner.platfom.adapter.state

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Created on 12.02.2020
 * @author YWeber */

sealed class LoadingDetailed(open val urlId: String) {

    data class HeaderTitle(val title: String) : LoadingDetailed("hash:${title.hashCode()}")

    data class Loading(
        override val urlId: String,
        val imageUrl: String,
        val title: String,
        val durationProgress: Long
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

    object DetailedDiff : DiffUtil.ItemCallback<LoadingDetailed>() {
        override fun areItemsTheSame(oldItem: LoadingDetailed, newItem: LoadingDetailed): Boolean =
            oldItem.urlId == newItem.urlId

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: LoadingDetailed, newItem: LoadingDetailed): Boolean =
            oldItem == newItem
    }

}
