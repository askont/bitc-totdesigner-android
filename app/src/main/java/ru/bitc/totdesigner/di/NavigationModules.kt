package ru.bitc.totdesigner.di

import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

/**
 * Created on 21.01.2020
 * @author YWeber */

object NavigationModules {

    private fun createCicerone() = Cicerone.create()
    private fun createRouter(cicerone: Cicerone<Router>) = cicerone.router
    private fun createNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.navigatorHolder


    fun navigationModule() = module {
        single { createCicerone() }
        single { createRouter(get()) }
        single { createNavigatorHolder(get()) }
    }
}

