package ru.bitc.totdesigner.model.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bitc.totdesigner.model.entity.loading.AllLoadingJob
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase(
    private val repository: DownloadPackageRepository,
    private val lessonRepository: LessonRepository
) {

    fun getAllLoadingPackage(lessonUrl: String): Flow<AllLoadingJob> {

        return flow {

        }
    }

    fun cancelAllJob() {

    }
}