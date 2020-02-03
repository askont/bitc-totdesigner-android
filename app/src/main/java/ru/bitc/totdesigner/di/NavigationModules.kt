package ru.bitc.totdesigner.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.bitc.totdesigner.platfom.navigation.LocalCiceroneHolder
import ru.bitc.totdesigner.platfom.navigation.LocalCiceroneHolder.Companion.APP_NAVIGATION
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

/**
 * Created on 21.01.2020
 * @author YWeber */
object NavigationModules {
    private fun createCicerone(routerHolder: LocalCiceroneHolder) = routerHolder.cicerone(APP_NAVIGATION)
    private fun createRouter(cicerone: Cicerone<Router>) = cicerone.router
    private fun createNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.navigatorHolder

    fun navigationModule() = module {
        single { LocalCiceroneHolder() }
        single { createCicerone(get()) }
        single { createRouter(get()) }
        single { createNavigatorHolder(get()) }
    }
}

