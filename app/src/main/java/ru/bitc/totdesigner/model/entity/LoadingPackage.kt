package ru.bitc.totdesigner.model.entity

/**
 * Created on 28.01.2020
 * @author YWeber */

sealed class LoadingPackage {
    data class Loading(val progress: Int) : LoadingPackage()
    data class StartUnzip(val progress: Int) : LoadingPackage()
    data class Error(val message: String) : LoadingPackage()
    object Finish : LoadingPackage()
}

