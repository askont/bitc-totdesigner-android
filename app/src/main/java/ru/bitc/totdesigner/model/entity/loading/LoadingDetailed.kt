package ru.bitc.totdesigner.model.entity.loading

/**
 * Created on 12.02.2020
 * @author YWeber */

sealed class LoadingDetailed(open val urlId: String) {
    data class Loading(
        override val urlId: String,
        val imageUrl: String,
        val title: String
    ) : LoadingDetailed(urlId)

    data class Finish(override val urlId: String) : LoadingDetailed(urlId)
}