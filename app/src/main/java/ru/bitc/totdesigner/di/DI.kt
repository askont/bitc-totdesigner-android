package ru.bitc.totdesigner.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bitc.totdesigner.catalog.CatalogViewModel
import ru.bitc.totdesigner.home.HomeViewModel
import ru.bitc.totdesigner.main.MainViewModel
import ru.bitc.totdesigner.model.CuratorSoapNetwork
import ru.bitc.totdesigner.model.iteractor.LessonUseCase
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.system.ResourceManager

object DI {

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
            CatalogViewModel(get(), get())
        }
        viewModel {
            MainViewModel()
        }
        viewModel {
            HomeViewModel(get())
        }
    }
}