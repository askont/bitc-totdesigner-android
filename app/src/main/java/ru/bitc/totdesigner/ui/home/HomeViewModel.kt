package ru.bitc.totdesigner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.interactor.HomeLessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.ui.home.state.HomeState

class HomeViewModel(
    private val resourceManager: ResourceManager,
    private val homeUseCase: HomeLessonUseCase
) : BaseViewModel() {

    private val action = MutableLiveData<HomeState>()

    private val currentState
        get() = action.value ?: defaultHomeData()

    val viewState: LiveData<HomeState>
        get() = action

    init {
        action.value = currentState
    }

    private fun defaultHomeData(): HomeState {
        val title = resourceManager.getString(R.string.title_home)
        val description = resourceManager.getString(R.string.description_home)
        return HomeState(State.Loaded, title, description, listOf())
    }
}