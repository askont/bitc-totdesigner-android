package ru.bitc.totdesigner.model.entity.loading

/**
 * Created on 05.02.2020
 * @author YWeber */

sealed class AllLoadingJob {
    data class Progress(val countJob: Int,val duration:Int) : AllLoadingJob()
    object Finish : AllLoadingJob()
}