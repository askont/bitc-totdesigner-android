package ru.bitc.totdesigner.ui

import kotlinx.coroutines.MainScope
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

/**
 * Created on 21.01.2020
 * @author YWeber */

class AppViewModel(
    private val router: Router,
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    fun setMainFlowScreen() {
        router.newRootChain(AppScreens.MainFlowScreen)
    }


    fun exit(){
        router.exit()
    }

}