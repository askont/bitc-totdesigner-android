package ru.bitc.totdesigner.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.iteractor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.main.state.LoadingItem
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
    navigatorHolder: NavigatorHolder
) : BaseViewModel(navigatorHolder) {

    private val action = MutableLiveData<MainState>()
    private val currentState
        get() = action.value ?: MainState(listOf(), false)
    val viewState: LiveData<MainState>
        get() = action

    init {
        launch {
            downloadNotifier.subscribeStatus().collect {
                val mock = createMock()
                val state = currentState.copy(
                    downloadsItem = mock, visibleDownload = mock.isNotEmpty()
                )
                action.value = state
            }
        }
    }

    private fun createMock(): List<LoadingItem> {
        return listOf(
            LoadingItem(
                100,
                resourceManager.getString(R.string.loading_progress_message)
            ),
            LoadingItem(
                12,
                resourceManager.getString(R.string.loading_progress_message)
            ),
            LoadingItem(
                32,
                resourceManager.getString(R.string.loading_progress_message)
            ),
            LoadingItem(
                58,
                resourceManager.getString(R.string.loading_progress_message)
            )
        )
    }

    fun selectHomeScreen(): Boolean {
        router.newRootScreen(MainScreens.HomeScreen)
        return true
    }

    fun selectCatalogScreen(): Boolean {
        router.replaceScreen(MainScreens.CatalogScreen)
        return true
    }

    fun selectOpenDisk(): Boolean {
        router.replaceScreen(AppScreens.MockScreen("open disk"))
        return true
    }

    fun selectSetting(): Boolean {
        router.replaceScreen(AppScreens.MockScreen("open setting"))
        return true
    }

    fun cancelLoading(item: LoadingItem) {
        val cancelItem = currentState.downloadsItem.toMutableList()
        cancelItem.remove(item)
        val newState = currentState.copy(downloadsItem = cancelItem, visibleDownload = cancelItem.isNotEmpty())
        action.value = newState
    }
}
