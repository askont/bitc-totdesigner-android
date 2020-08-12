package ru.bitc.totdesigner.ui.main

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.loading.ProcessDownloading
import ru.bitc.totdesigner.model.interactor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.ChangeBackgroundNotifier
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.main.state.MainState
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

/*
 * Created on 2019-12-03
 * @author YWeber
 */
class MainViewModel(
        private val downloadNotifier: DownloadNotifier,
        private val resourceManager: ResourceManager,
        private val downloadUseCase: DownloadPackageUseCase,
        private val router: Router,
        changeBackgroundNotifier: ChangeBackgroundNotifier,
        navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    private val action = MutableLiveData<MainState>()

    private val currentState
        get() = action.value
                ?: MainState.DEFAULT.copy(background = downloadUseCase.currentBackground, isBlur = downloadUseCase.currentBackground != R.drawable.img_totdesign)

    val viewState: LiveData<MainState>
        get() = action

    private var allLoadingJobs: Job

    init {
        action.value = currentState
        allLoadingJobs = createEventJob()
        changeBackgroundNotifier.action
                .onEach {
                    downloadUseCase.saveBackgroundPref(it.drawableRes)
                    updateState(background = it.drawableRes, isBlur = it.drawableRes != R.drawable.img_totdesign)
                }
                .launchIn(viewModelScope)
        launch {
            downloadNotifier.subscribeAllVisible()
                    .collect {
                        updateState(visibleLoading = it, background = downloadUseCase.currentBackground)
                    }
        }
    }

    private fun createEventJob(): Job {
        return downloadNotifier.subscribeStatus()
                .flatMapLatest { downloadUseCase.processTaskEventLoadingCount(it.lessonUrl, it.lessonName, it.isDelete) }
                .onEach { progress ->
                    downloadNotifier.updateLessonsPreview()
                    when (progress) {
                        is ProcessDownloading.Count -> {
                            val message = resourceManager.getString(R.string.loading_progress_messages, progress.countJob)
                            updateState(
                                    message = message,
                                    progressDuration = progress.duration,
                                    finishLoading = true,
                                    isError = false
                            )
                        }
                        is ProcessDownloading.Finish -> {
                            updateState(finishLoading = false, isError = false)
                        }
                        is ProcessDownloading.Error -> {
                            updateState(finishLoading = false, isError = true)
                        }
                    }
                }
                .launchIn(viewModelScope)
    }

    private fun updateState(
            message: String = currentState.messageLoading,
            progressDuration: Int = currentState.durationProgress,
            finishLoading: Boolean = currentState.finishLoading,
            visibleLoading: Boolean = currentState.visibleLoading,
            isError: Boolean = currentState.isError,
            isBlur: Boolean = currentState.isBlur,
            @DrawableRes background: Int = currentState.background
    ) {
        val newState = currentState.copy(
                messageLoading = message,
                finishLoading = finishLoading,
                durationProgress = progressDuration,
                visibleLoading = visibleLoading,
                isError = isError,
                isBlur = isBlur,
                background = background
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
        router.newRootScreen(AppScreens.SettingScreen)
        return true
    }

    fun cancelAllJobLoading() {
        downloadUseCase.cancelAllJob()
        allLoadingJobs.cancelChildren()
        updateState(finishLoading = false, isError = false)
        allLoadingJobs = createEventJob()
    }

    fun navigateToLoadingDetails() {
        downloadNotifier.eventVisible(false)
        router.navigateTo(MainScreens.LoadingDetailedScreen)
    }
}
