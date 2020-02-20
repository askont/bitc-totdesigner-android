package ru.bitc.totdesigner.ui.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.interactor.DownloadPackageUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.LoadingDetailed
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
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
            loadingUseCase.getListPairLoadingAndPreview()
                .map { loads ->
                    loads.map { createDetailedLoading(it.first, it.second) }
                }
                .collect {
                    sortRenderTypeItems(it)
                }
        }
    }


    private fun sortRenderTypeItems(items: List<LoadingDetailed>) {
        val renderList = mutableListOf<LoadingDetailed>()
        val listLoading = items.filterIsInstance<LoadingDetailed.Loading>()
        if (listLoading.isNotEmpty()) {
            renderList.add(LoadingDetailed.HeaderTitle("Загрузки"))
            renderList.addAll(listLoading)
        }
        val listFinish = items.filterIsInstance<LoadingDetailed.Finish>()
        if (listFinish.isNotEmpty()) {
            renderList.add(LoadingDetailed.HeaderTitle("Готовы к запуску"))
            renderList.addAll(listFinish)
        }
        val listError = items.filterIsInstance<LoadingDetailed.Error>()
        if (listError.isNotEmpty()) {
            renderList.add(LoadingDetailed.HeaderTitle("Произошла ошибка"))
            renderList.addAll(listError)
        }
        val newState = currentState.copy(loadingMiniItems = renderList)
        action.value = newState
    }

    private fun createDetailedLoading(
        loading: LoadingPackage,
        lessons: PreviewLessons.Lesson
    ): LoadingDetailed = when (loading) {
        is LoadingPackage.Loading -> {
            LoadingDetailed.Loading(lessons.lessonUrl, lessons.imageUrl, lessons.title, loading.durationProgress)
        }
        is LoadingPackage.Finish -> {
            LoadingDetailed.Finish(lessons.lessonUrl, lessons.imageUrl, lessons.title)
        }
        is LoadingPackage.Error -> {
            LoadingDetailed.Error(lessons.lessonUrl, lessons.imageUrl, lessons.title)
        }

    }

    fun userEvent(detailed: LoadingDetailed) {
        when (detailed) {
            is LoadingDetailed.Finish -> {
                //TODO Open lesson
            }
            is LoadingDetailed.Loading -> {
                downloadNotifier.eventStatus(detailed.urlId, true)
            }
            is LoadingDetailed.Error -> {
                //TODO Retry job loading
            }
        }
    }


    fun onVisible() {
        downloadNotifier.eventVisible(true)
    }

    fun backTo() {
        mainRouter.exit()
    }
}