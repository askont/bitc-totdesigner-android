package ru.bitc.totdesigner.ui.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.interactor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailed
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailedState
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
            loadingUseCase.getListLoadingPackageJob()
                .collect { detailed ->
                    val detailedLoading = createDetailedLoading(detailed.first, detailed.second)
                    addItems(detailedLoading)
                }
        }
    }

    private fun addItems(item: LoadingDetailed) {
        val mutableItemsList = currentState.loadingMiniItems.toMutableList()
        mutableItemsList.add(item)
        val newState = currentState.copy(loadingMiniItems = mutableItemsList.filterIsInstance<LoadingDetailed.Loading>())
        action.value = newState

    }

    private fun createDetailedLoading(
        loading: LoadingPackage,
        lessons: PreviewLessons.Lesson
    ): LoadingDetailed = when (loading) {
        is LoadingPackage.Loading -> {
            LoadingDetailed.Loading(lessons.lessonUrl, lessons.imageUrl, lessons.title)
        }
        is LoadingPackage.Finish -> {
            LoadingDetailed.Finish(lessons.lessonUrl, lessons.imageUrl, lessons.title)
        }
        is LoadingPackage.Error -> {
            LoadingDetailed.Error(lessons.lessonUrl, lessons.imageUrl, lessons.title)
        }

    }


    fun onVisible() {
        downloadNotifier.eventVisible(true)
    }

    fun backTo() {
        mainRouter.exit()
    }
}