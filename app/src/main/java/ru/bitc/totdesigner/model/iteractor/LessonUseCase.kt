package ru.bitc.totdesigner.model.iteractor

import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.platfom.state.State
import timber.log.Timber

/*
 * Created on 2019-12-19
 * @author YWeber
 */
class LessonUseCase(private val repository: LessonRepository) {

    suspend fun getLessonPreview(
        onState: (State) -> Unit,
        async: (PreviewLessons) -> Unit
    ) {
        try {
            onState(State.Loading)
            val lessons = repository.getPreviewLessons()
            async(lessons)
            onState.invoke(State.Loaded)
        } catch (e: Exception) {
            Timber.e(e)
            onState.invoke(State.Error(error = e))
        }
    }

    suspend fun searchLesson(nameQuest:String ,onState: (State) -> Unit,
                  async: (PreviewLessons) -> Unit){
        try {
            onState(State.Loading)
            val lessons = searchPreviewLessons(nameQuest,repository.getPreviewLessons())
            async(lessons)
            onState.invoke(State.Loaded)
        } catch (e: Exception) {
            Timber.e(e)
            onState.invoke(State.Error(error = e))
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