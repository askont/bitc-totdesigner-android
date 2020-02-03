package ru.bitc.totdesigner.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.LoadingPackage
import ru.bitc.totdesigner.model.iteractor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.system.notifier.model.FreeDownloadPackage
import ru.bitc.totdesigner.ui.main.state.LoadingItem
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
        get() = action.value ?: MainState(listOf(), false)
    val viewState: LiveData<MainState>
        get() = action
    private val mapRequest = mutableMapOf<String, Job>()

    init {
        launch {
            downloadNotifier.subscribeStatus()
                .collect {
                    val newItem = LoadingItem(it.lessonUrl, 1, "Загрузка \"${it.nameLesson}\"")
                    val downloadsItem = currentState.downloadsItem.toMutableList()
                    downloadsItem.add(newItem)
                    action.value = currentState.copy(downloadsItem = downloadsItem, visibleDownload = true)
                    mapRequest[it.lessonUrl] = loadPackage(it)
                }
        }
    }

    private fun loadPackage(free: FreeDownloadPackage) = launch {

        downloadUseCase.loadPackage(free.lessonUrl).collect { progress ->
            Timber.d("$progress")
            when (progress) {
                is LoadingPackage.Loading -> {
                    updateLoading(progress)
                }
                is LoadingPackage.Finish -> {
                    finishRemoveItem(progress)
                }
            }
        }
    }

    private fun finishRemoveItem(progress: LoadingPackage) {
        val finishItem = currentState.downloadsItem.find { it.urlId == progress.urlId }
        val removedItems = currentState.downloadsItem.toMutableList()
        removedItems.remove(finishItem)
        action.value = currentState.copy(downloadsItem = removedItems)
    }

    private fun updateLoading(progress: LoadingPackage.Loading) {
        val updateItems = currentState.downloadsItem
            .map {
                if (it.urlId == progress.urlId) {
                    it.copy(progress = progress.progress)
                } else it
            }
        action.value = currentState.copy(downloadsItem = updateItems)
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
        val job = mapRequest[item.urlId]
        job?.cancel()
        val cancelItem = currentState.downloadsItem.toMutableList()
        cancelItem.remove(item)
        val newState = currentState.copy(downloadsItem = cancelItem, visibleDownload = cancelItem.isNotEmpty())
        action.value = newState
    }
}
