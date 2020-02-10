package ru.bitc.totdesigner.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.loading.AllLoadingJob
import ru.bitc.totdesigner.model.iteractor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.main.state.MainState
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import timber.log.Timber

/*
 * Created on 2019-12-03
 * @author YWeber
 */
class MainViewModel(
    private val downloadNotifier: DownloadNotifier,
    private val resourceManager: ResourceManager,
    private val downloadUseCase: DownloadPackageUseCase,
    private val router: Router,
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    private val action = MutableLiveData<MainState>()
    private val currentState
        get() = action.value ?: MainState("", 0, false)
    val viewState: LiveData<MainState>
        get() = action
    private var allLoadingJobs: Job

    init {
        action.value = currentState
        allLoadingJobs = createEventJob()
    }

    private fun createEventJob(): Job {
        return launch {
            downloadNotifier.subscribeStatus()
                .flatMapLatest { downloadUseCase.loadPackage(it.lessonUrl) }
                .onEach { Timber.e("test test test $it") }
                .collect { progress ->
                    when (progress) {
                        is AllLoadingJob.Progress -> {
                            val message =
                                resourceManager.getString(R.string.loading_progress_messages, progress.countJob)
                            updateState(message = message, progressDuration = progress.duration, visibleLoading = true)
                        }
                        is AllLoadingJob.Finish -> {
                            updateState(visibleLoading = false)
                        }
                    }
                }
        }
    }

    private fun updateState(
        message: String = currentState.messageLoading, progressDuration: Int = currentState.durationProgress,
        visibleLoading: Boolean = currentState.visibleDownload
    ) {
        val newState = currentState.copy(
            messageLoading = message,
            visibleDownload = visibleLoading,
            durationProgress = progressDuration
        )
        action.value = newState
    }

    fun selectHomeScreen(): Boolean {
        router.newRootScreen(MainScreens.HomeScreen)
        return true
    }

    fun selectCatalogScreen(): Boolean {
        router.newRootScreen(MainScreens.CatalogScreen)
        return true
    }

    fun selectOpenDisk(): Boolean {
        router.newRootScreen(AppScreens.MockScreen("open disk"))
        return true
    }

    fun selectSetting(): Boolean {
        router.newRootScreen(AppScreens.MockScreen("open setting"))
        return true
    }

    fun cancelAllJobLoading() {
        downloadUseCase.cancelAllJob()
        allLoadingJobs.cancel()
        updateState(visibleLoading = false)
        allLoadingJobs = createEventJob()
    }

    fun navigateToLoadingDetails() {
        router.navigateTo(MainScreens.LoadingDetailedScreen)
    }
}
