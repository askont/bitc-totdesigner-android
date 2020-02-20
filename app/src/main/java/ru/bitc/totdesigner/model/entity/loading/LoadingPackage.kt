package ru.bitc.totdesigner.model.entity.loading

/**
 * Created on 28.01.2020
 * @author YWeber */

sealed class LoadingPackage(open val urlId: String) {
    data class Loading(override val urlId: String, val durationProgress: Long) : LoadingPackage(urlId)
    data class Error(override val urlId: String, val message: String) : LoadingPackage(urlId)
    data class Finish(override val urlId: String) : LoadingPackage(urlId)
}

