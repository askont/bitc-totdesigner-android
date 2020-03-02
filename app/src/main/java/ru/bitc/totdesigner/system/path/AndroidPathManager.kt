package ru.bitc.totdesigner.system.path

import android.content.Context
import android.os.Environment

/**
 * Created on 02.03.2020
 * @author YWeber */

class AndroidPathManager(private val context: Context) : PathManager {
    override val externalDirLocalFile
        get() = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.toString() ?: ""
}