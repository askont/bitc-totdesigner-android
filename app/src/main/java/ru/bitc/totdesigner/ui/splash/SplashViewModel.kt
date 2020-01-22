package ru.bitc.totdesigner.ui.splash

import kotlinx.coroutines.delay
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

/**
 * Created on 22.01.2020
 * @author YWeber */

class SplashViewModel(
    private val router: Router,
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {
    fun delayNextScreen() {
        launch {
            delay(DELAY_SPLASH)
            router.newRootScreen(AppScreens.AppFlowScreen)
        }
    }

    companion object{
        private const val DELAY_SPLASH = 800L
    }
}