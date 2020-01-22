package ru.bitc.totdesigner.platfom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.bitc.totdesigner.platfom.state.State
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import timber.log.Timber

/*
 * Created on 2019-11-25
 * @author YWeber
 */
abstract class BaseViewModel(private val navigatorHolder: NavigatorHolder? = null) : ViewModel() {


    protected open fun handleState(state: State) {
        Timber.d(state.toString())
    }

    protected fun launch(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.Main) { func.invoke() }

    fun addNavigator(navigator: Navigator){
        navigatorHolder?.setNavigator(navigator)
    }

    fun deleteNavigator(){
        navigatorHolder?.removeNavigator()
    }
}