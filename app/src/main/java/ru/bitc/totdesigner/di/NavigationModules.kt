package ru.bitc.totdesigner.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.bitc.totdesigner.platfom.navigation.LocalCiceroneHolder
import ru.bitc.totdesigner.platfom.navigation.LocalCiceroneHolder.Companion.APP_NAVIGATION
import ru.bitc.totdesigner.platfom.navigation.LocalCiceroneHolder.Companion.MAIN_NAVIGATION
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

/**
 * Created on 21.01.2020
 * @author YWeber */
object NavigationModules {
    val appCicerone = named(APP_NAVIGATION)
    val appRouter = named("${APP_NAVIGATION}Router")
    val appHolder = named("${APP_NAVIGATION}Holder")

    val mainCicerone = named(MAIN_NAVIGATION)
    val mainRouter = named("${MAIN_NAVIGATION}Router")
    val mainHolder = named("${MAIN_NAVIGATION}Holder")
    private fun createCicerone(routerHolder: LocalCiceroneHolder, keyCicerone: String) =
        routerHolder.cicerone(keyCicerone)

    private fun createRouter(cicerone: Cicerone<Router>) = cicerone.router
    private fun createNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.navigatorHolder

    fun navigationModule() = module {
        single { LocalCiceroneHolder() }
        // app navigation stack
        single(qualifier = appCicerone) { createCicerone(get(), APP_NAVIGATION) }
        single(qualifier = appRouter) { createRouter(get(qualifier = appCicerone)) }
        single(qualifier = appHolder) { createNavigatorHolder(get(qualifier = appCicerone)) }

        // main navigation stack
        single(qualifier = mainCicerone) { createCicerone(get(), MAIN_NAVIGATION) }
        single(qualifier = mainRouter) { createRouter(get(qualifier = mainCicerone)) }
        single(qualifier = mainHolder) { createNavigatorHolder(get(qualifier = mainCicerone)) }
    }
}

