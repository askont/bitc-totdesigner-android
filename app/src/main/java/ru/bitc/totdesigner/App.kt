package ru.bitc.totdesigner

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.bitc.totdesigner.di.AppModules
import ru.bitc.totdesigner.di.NavigationModules
import ru.bitc.totdesigner.di.SystemModule
import timber.log.Timber

/*
 * Created on 2019-11-24
 * @author YWeber
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            val asTree = Timber.DebugTree()
            Timber.plant(asTree)
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                listOf(
                    AppModules.appModule(),
                    AppModules.networkModule(),
                    AppModules.viewModelModule(),
                    NavigationModules.navigationModule(),
                    SystemModule.notifierModule()
                )
            )
        }
    }
}