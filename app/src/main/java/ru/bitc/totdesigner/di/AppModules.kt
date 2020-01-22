package ru.bitc.totdesigner.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bitc.totdesigner.ui.catalog.CatalogViewModel
import ru.bitc.totdesigner.ui.home.HomeViewModel
import ru.bitc.totdesigner.ui.main.MainViewModel
import ru.bitc.totdesigner.model.CuratorSoapNetwork
import ru.bitc.totdesigner.model.iteractor.LessonUseCase
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.ui.AppViewModel
import ru.bitc.totdesigner.ui.splash.SplashViewModel

object AppModules {

    fun networkModule() = module {
        single { CuratorSoapNetwork.createHttpClient() }
        single { CuratorSoapNetwork.createRetrofit(get()) }
        single { CuratorSoapNetwork.createSoapApi(get()) }

    }

    fun appModule() = module {
        single { ResourceManager(get()) }
        // lesson
        single { LessonUseCase(get()) }
        single { LessonRepository(get()) }
    }

    fun viewModelModule() = module {
        viewModel {
            AppViewModel(get(),get())
        }

        viewModel {
            CatalogViewModel(get(), get())
        }
        viewModel {
            MainViewModel(get(),get())
        }
        viewModel {
            HomeViewModel(get())
        }

        viewModel { SplashViewModel(get(),get()) }
    }
}