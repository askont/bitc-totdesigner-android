package ru.bitc.totdesigner.ui.catalog.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.iteractor.LessonUseCase
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.ui.catalog.dialog.state.DownLoadViewState

/**
 * Created on 26.01.2020
 * @author YWeber */

class DownLoadViewModel(
    private val nameQuest: String,
    private val resourceManager: ResourceManager,
    private val useCase: LessonUseCase
) : BaseViewModel() {

    private val action = MutableLiveData<DownLoadViewState>()

    private val currentState: DownLoadViewState
        get() = action.value ?: DownLoadViewState.Free("", "", "")

    val viewState: LiveData<DownLoadViewState>
        get() = action

    fun initState() {
        launch {
            useCase.getLesson(nameQuest, ::handleLessons)
        }
    }

    private fun handleLessons(lesson: PreviewLessons.Lesson) {
        val newState = when (lesson.category) {
            PreviewLessons.Category.FREE -> DownLoadViewState.Free(
                lesson.title,
                resourceManager.getString(R.string.free_download_description),
                lesson.imageUrl
            )
            PreviewLessons.Category.PAID -> DownLoadViewState.Paid(
                lesson.title,
                resourceManager.getString(R.string.paid_download_description),
                lesson.imageUrl
            )
            else -> currentState
        }

        action.value = newState
    }

    fun download() {

    }

}