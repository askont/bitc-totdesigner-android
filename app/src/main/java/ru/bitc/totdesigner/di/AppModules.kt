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
import ru.bitc.totdesigner.system.StringUuidBuilder
import ru.bitc.totdesigner.ui.AppViewModel
import ru.bitc.totdesigner.ui.catalog.CatalogViewModel
import ru.bitc.totdesigner.ui.catalog.dialog.DownloadViewModel
import ru.bitc.totdesigner.ui.home.HomeViewModel
import ru.bitc.totdesigner.ui.home.dialog.DetailedLessonViewModel
import ru.bitc.totdesigner.ui.interaction.InteractionViewModel
import ru.bitc.totdesigner.ui.loading.LoadingDetailedViewModel
import ru.bitc.totdesigner.ui.main.MainViewModel
import ru.bitc.totdesigner.ui.setting.SettingViewModel
import ru.bitc.totdesigner.ui.splash.SplashViewModel
import ru.bitc.totdesigner.ui.statistic.StatisticMainVIewModel

object AppModules {

    fun networkModule() = module {
        single { CuratorSoapNetwork.createHttpClient() }
        single { CuratorSoapNetwork.createRetrofit(client = get()) }
        single { CuratorSoapNetwork.createSoapApi(retrofit = get()) }
    }

    fun appModule() = module {
        // lesson
        single { LessonUseCase(repository = get(), dispatcher = get()) }
        single { StringUuidBuilder() }
        single {
            LessonRepository(
                api = get(),
                toEntityPreviewConverter = get(),
                pathDao = get(),
                prefsStore = get()
            )
        }

        //download
        single {
            DownloadPackageRepository(
                api = get(),
                path = get(),
                unzip = get(),
                pathDao = get(),
                dispatcher = get()
            )
        }
        single { DownloadPackageUseCase(repository = get(), lessonRepository = get(), dispatcher = get()) }

        //home
        single {
            HomeLessonRepository(
                pathDao = get(),
                dispatcher = get(),
                pathManager = get(),
                converterXmlToModel = get()
            )
        }
        single { HomeLessonUseCase(repository = get()) }

        //start lesson
        single { StartInteractionRepository(pathDao = get(), pathManager = get(), converter = get()) }
        single { StartInteractionUseCase(repository = get(), offsetWindowsSize = get(), dispatcher = get()) }
    }

    fun viewModelModule() = module {
        viewModel {
            SplashViewModel(
                router = get(qualifier = NavigationModules.appRouter),
                navigatorHolder = get(qualifier = NavigationModules.appHolder)
            )
        }
        viewModel {
            AppViewModel(
                router = get(qualifier = NavigationModules.appRouter),
                navigatorHolder = get(qualifier = NavigationModules.appHolder)
            )
        }
        viewModel {
            CatalogViewModel(
                mainRouter = get(qualifier = NavigationModules.mainRouter),
                resourceManager = get(),
                useCase = get(),
                downloadNotifier = get()
            )
        }
        viewModel {
            get<LocalCiceroneHolder>()
            MainViewModel(
                downloadNotifier = get(),
                resourceManager = get(),
                downloadUseCase = get(),
                router = get(qualifier = NavigationModules.mainRouter),
                changeBackgroundNotifier = get(),
                navigatorHolder = get(qualifier = NavigationModules.mainHolder)
            )
        }
        viewModel {
            HomeViewModel(
                resourceManager = get(),
                homeUseCase = get(),
                downloadNotifier = get(),
                router = get(qualifier = NavigationModules.mainRouter)
            )
        }
        viewModel { (nameQuest: String) ->
            DownloadViewModel(
                nameQuest = nameQuest,
                downloadNotifier = get(),
                resourceManager = get(),
                useCase = get()
            )
        }
        viewModel {
            LoadingDetailedViewModel(
                mainRouter = get(qualifier = NavigationModules.mainRouter),
                loadingUseCase = get(),
                downloadNotifier = get()
            )
        }
        viewModel { (remotePath: String) ->
            DetailedLessonViewModel(
                remotePath = remotePath,
                useCase = get(),
                downloadNotifier = get(),
                resourceManager = get(),
                router = get(qualifier = NavigationModules.mainRouter)
            )
        }

        viewModel { (lessonPath: String) ->
            InteractionViewModel(
                lessonPath = lessonPath,
                useCase = get(),
                router = get(qualifier = NavigationModules.appRouter),
                navigatorHolder = get(qualifier = NavigationModules.appHolder),
                uuidBuilder = get(),
                stateMapper = get()
            )
        }

        viewModel { SettingViewModel(settingPlatform = get(), notifier = get()) }

        viewModel { StatisticMainVIewModel() }
    }
}