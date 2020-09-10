package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.bitc.totdesigner.model.entity.LessonCatalogInteractive
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import timber.log.Timber

/*
 * Created on 2019-12-19
 * @author YWeber
 */
class LessonUseCase(
    private val repository: LessonRepository,
    private val dispatcher: DispatcherProvider
) {

    suspend fun getLessonPreview(
        onState: (State) -> Unit,
        async: (PreviewLessons) -> Unit
    ) {
        try {
            onState(State.Loading)
            val lessons = repository.getFilterLocalPreviewLessons()
            async(lessons)
            onState.invoke(State.Loaded)
        } catch (e: Exception) {
            Timber.e(e)
            onState.invoke(State.Error(error = e))
        }
    }

    suspend fun searchLessons(
        nameQuest: String, onState: (State) -> Unit,
        async: (PreviewLessons) -> Unit
    ) {
        try {
            onState(State.Loading)
            val lessons = searchPreviewLessons(nameQuest, repository.getFilterLocalPreviewLessons())
            async(lessons)
            onState.invoke(State.Loaded)
        } catch (e: Exception) {
            Timber.e(e)
            onState.invoke(State.Error(error = e))
        }
    }

    fun lessonRemoteUrlByName(lessonName: String): Flow<LessonCatalogInteractive> = flow {
        val checkLessonUrlInLocal = repository.getRemoteLessonUrlByName(lessonName)
        if (checkLessonUrlInLocal.isEmpty()) {
            emit(LessonCatalogInteractive.Empty)
        } else {
            emit(LessonCatalogInteractive.Find(checkLessonUrlInLocal))
        }
    }.flowOn(dispatcher.io)

    suspend fun getLesson(name: String, eventElement: (PreviewLessons.Lesson) -> Unit) {
        try {
            val lesson = repository.getFilterLocalPreviewLessons().previews.find { it.title == name } ?: return
            eventElement(lesson)
        } catch (e: Exception) {
            Timber.e("Not found element by name = $name.")
        }
    }

    private fun searchPreviewLessons(
        nameQuest: String,
        previewLessons: PreviewLessons
    ): PreviewLessons {
        return if (nameQuest.isNotEmpty()) {
            previewLessons.copy(previews = previewLessons.previews.filter {
                it.title.contains(
                    nameQuest,
                    true
                )
            })
        } else previewLessons
    }
}