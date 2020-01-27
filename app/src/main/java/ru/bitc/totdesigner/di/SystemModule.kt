package ru.bitc.totdesigner.di

import org.koin.dsl.module
import ru.bitc.totdesigner.system.notifier.DownloadNotifier

/**
 * Created on 27.01.2020
 * @author YWeber */
 
object SystemModule {

    fun notifierModule() = module {
        single { DownloadNotifier() }
    }

}