package ru.bitc.totdesigner.model.iteractor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import ru.bitc.totdesigner.model.entity.LoadingPackage
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase(private val repository: DownloadPackageRepository) {

    fun loadPackage(lessonUrl: String): Flow<LoadingPackage> {
        return repository.downloadPackage(lessonUrl)
    }
}