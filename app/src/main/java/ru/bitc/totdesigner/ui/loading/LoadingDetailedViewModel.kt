package ru.bitc.totdesigner.ui.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.interactor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailedState
import ru.bitc.totdesigner.ui.loading.state.LoadingMiniItem
import ru.terrakok.cicerone.Router

/**
 * Created on 04.02.2020
 * @author YWeber */

class LoadingDetailedViewModel(
    private val mainRouter: Router,
    private val loadingUseCase: DownloadPackageUseCase,
    private val downloadNotifier: DownloadNotifier
) : BaseViewModel() {

    private val action = MutableLiveData<LoadingDetailedState>()
    private val currentState
        get() = action.value ?: LoadingDetailedState(listOf())

    val state: LiveData<LoadingDetailedState>
        get() = action

    init {
        launch {
            loadingUseCase.getListLoadingPackage()
                .collect { detailedList ->
                    detailedList.forEach {
                        addItems(LoadingMiniItem(it.urlId, it.title, it.imageUrl, 100))
                    }
                }
        }
    }

    private fun addItems(item: LoadingMiniItem) {
        val mutableItemsList = currentState.loadingMiniItems.toMutableList()
        mutableItemsList.add(item)
        val newState = currentState.copy(mutableItemsList)
        action.value = newState

    }

    fun onVisible() {
        downloadNotifier.eventVisible(true)
    }

    fun backTo() {
        mainRouter.exit()
    }
}