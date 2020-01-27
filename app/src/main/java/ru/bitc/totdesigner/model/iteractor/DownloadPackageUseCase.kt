package ru.bitc.totdesigner.model.iteractor

import kotlinx.coroutines.delay

/**
 * Created on 27.01.2020
 * @author YWeber */

class DownloadPackageUseCase {

    suspend fun load(): String {
        delay(1000)
        return "test"
    }
}