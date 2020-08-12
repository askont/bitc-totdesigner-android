package ru.bitc.totdesigner.platfom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.terrakok.cicerone.NavigatorHolder

/**
 * Created on 05.08.2020
 * @author YWeber */

abstract class BaseActionViewModel<T>(
        navigatorHolder: NavigatorHolder? = null,
        private val initState: T,
        protected val isStartActionState: Boolean = true
) : BaseViewModel(navigatorHolder) {
    val state: LiveData<T>
        get() = action

    protected val currentState
        get() = action.value ?: initState


    protected val action = MutableLiveData<T>()

    init {
        if (isStartActionState) {
            updateState(state = initState)
        }
    }

    protected fun updateState(state: T) {
        action.value = state
    }
}