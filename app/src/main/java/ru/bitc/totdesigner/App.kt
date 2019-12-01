package ru.bitc.totdesigner

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.bitc.totdesigner.di.DI
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
            modules(listOf(DI.appModule(), DI.networkModule(), DI.viewModelModule()))
        }
    }
}