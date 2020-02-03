package ru.bitc.totdesigner.platfom.state

/*
 * Created on 2019-12-08
 * @author YWeber
 */
sealed class State {

    object Loaded : State()
    object Loading : State()
    data class Error(val message: String = "", val error: Throwable? = null) : State()
}