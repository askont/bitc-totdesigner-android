package ru.bitc.totdesigner.di

import org.koin.dsl.module
import ru.bitc.totdesigner.model.database.core.AppDatabaseBuilder
import ru.bitc.totdesigner.model.prefs.AndroidPrefsStore
import ru.bitc.totdesigner.model.prefs.PrefsStore

/**
 * Created on 03.03.2020
 * @author YWeber */

object DatabaseModules {
    fun appDatabaseModule() = module {
        single { AppDatabaseBuilder.createDatabase(get()) }
        single { AppDatabaseBuilder.createPathDao(get()) }
        single<PrefsStore> { AndroidPrefsStore(get()) }
    }
}