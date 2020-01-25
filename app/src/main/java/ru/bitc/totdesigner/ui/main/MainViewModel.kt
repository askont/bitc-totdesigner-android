package ru.bitc.totdesigner.ui.main

import kotlinx.coroutines.delay
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

/*
 * Created on 2019-12-03
 * @author YWeber
 */
class MainViewModel(
    private val router: Router,
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    fun selectHomeScreen(): Boolean {
        router.newRootScreen(MainScreens.HomeScreen)
        return true
    }

    fun selectCatalogScreen(): Boolean {
        router.replaceScreen(MainScreens.CatalogScreen)
        return true
    }

    fun selectOpenDisk():Boolean{
        router.replaceScreen(AppScreens.MockScreen("open disk"))
        return true
    }

    fun selectSetting(): Boolean {
        router.replaceScreen(AppScreens.MockScreen("open setting"))
        return true
    }
}
