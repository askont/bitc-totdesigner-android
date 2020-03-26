package ru.bitc.totdesigner.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bitc.totdesigner.model.http.retrofit.CuratorSoapNetwork
import ru.bitc.totdesigner.model.interactor.DownloadPackageUseCase
import ru.bitc.totdesigner.model.interactor.HomeLessonUseCase
import ru.bitc.totdesigner.model.interactor.LessonUseCase
import ru.bitc.totdesigner.model.interactor.StartInteractionUseCase
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.HomeLessonRepository
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.model.repository.StartInteractionRepository
import ru.bitc.totdesigner.platfom.navigation.LocalCiceroneHolder
import ru.bitc.totdesigner.ui.AppViewModel
import ru.bitc.totdesigner.ui.catalog.CatalogViewModel
import ru.bitc.totdesigner.ui.catalog.dialog.DownloadViewModel
import ru.bitc.totdesigner.ui.home.HomeViewModel
import ru.bitc.totdesigner.ui.home.dialog.DetailedLessonViewModel
import ru.bitc.totdesigner.ui.interaction.InteractionViewModel
import ru.bitc.totdesigner.ui.loading.LoadingDetailedViewModel
import ru.bitc.totdesigner.ui.main.MainViewModel
import ru.bitc.totdesigner.ui.splash.SplashViewModel

object AppModules {

    fun networkModule() = module {
        single { CuratorSoapNetwork.createHttpClient() }
        single { CuratorSoapNetwork.createRetrofit(get()) }
        single { CuratorSoapNetwork.createSoapApi(get()) }

    }

    fun appModule() = module {
        // lesson
        single { LessonUseCase(get()) }
        single { LessonRepository(get(), get(), get()) }

        //download
        single { DownloadPackageRepository(get(), get(), get(), get(), get()) }
        single { DownloadPackageUseCase(get(), get(), get()) }

        //home
        single { HomeLessonRepository(get(), get(), get(), get()) }
        single { HomeLessonUseCase(get()) }

        //start lesson
        single { StartInteractionRepository(get(), get(), get()) }
        single { StartInteractionUseCase(get(), get(), get()) }
    }

    fun viewModelModule() = module {
        viewModel {
            SplashViewModel(
                get(qualifier = NavigationModules.appRouter),
                get(qualifier = NavigationModules.appHolder)
            )
        }
        viewModel {
            AppViewModel(
                get(qualifier = NavigationModules.appRouter),
                get(qualifier = NavigationModules.appHolder)
            )
        }
        viewModel {
            CatalogViewModel(
                get(qualifier = NavigationModules.mainRouter),
                get(),
                get(),
                get()
            )
        }
        viewModel {
            get<LocalCiceroneHolder>()
            MainViewModel(
                get(),
                get(),
                get(),
                get(qualifier = NavigationModules.mainRouter),
                get(qualifier = NavigationModules.mainHolder)
            )
        }
        viewModel {
            HomeViewModel(
                get(),
                get(),
                get(),
                get(qualifier = NavigationModules.mainRouter)
            )
        }
        viewModel { (nameQuest: String) ->
            DownloadViewModel(
                nameQuest,
                get(),
                get(),
                get()
            )
        }
        viewModel {
            LoadingDetailedViewModel(
                get(qualifier = NavigationModules.mainRouter),
                get(),
                get()
            )
        }
        viewModel { (remotePath: String) ->
            DetailedLessonViewModel(
                remotePath,
                get(),
                get(),
                get(),
                get(qualifier = NavigationModules.mainRouter)
            )
        }

        viewModel { (lessonPath: String) ->
            InteractionViewModel(
                lessonPath,
                get(),
                get(qualifier = NavigationModules.appRouter),
                get(qualifier = NavigationModules.appHolder)
            )
        }
    }
}