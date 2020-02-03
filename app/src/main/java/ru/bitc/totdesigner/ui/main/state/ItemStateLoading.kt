package ru.bitc.totdesigner.ui.main.state

import ru.bitc.totdesigner.system.notifier.model.FreeDownloadPackage

/**
 * Created on 27.01.2020
 * @author YWeber */

data class LoadingItem(
    val freeDownloadPackage: FreeDownloadPackage,
    val progress: Int, val message: String
)