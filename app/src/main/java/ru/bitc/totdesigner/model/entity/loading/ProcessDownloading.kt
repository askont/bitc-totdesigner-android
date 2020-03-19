package ru.bitc.totdesigner.model.entity.loading

/**
 * Created on 05.02.2020
 * @author YWeber */

sealed class ProcessDownloading {
    data class Count(val countJob: Int, val duration: Int) : ProcessDownloading()
    object Finish : ProcessDownloading()
    object Error : ProcessDownloading()

}