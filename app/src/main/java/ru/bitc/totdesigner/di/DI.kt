package ru.bitc.totdesigner.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bitc.totdesigner.catalog.CatalogViewModel
import ru.bitc.totdesigner.home.HomeViewModel
import ru.bitc.totdesigner.main.MainViewModel
import ru.bitc.totdesigner.system.ResourceManager

object DI {
    fun appModule() = module {
        single { ResourceManager(get()) }
    }

    fun viewModelModule() = module {
        viewModel {
            CatalogViewModel(get())
        }
        viewModel {
            MainViewModel()
        }
        viewModel {
            HomeViewModel(get())
        }
    }

    fun networkModule() = module {

    }
}