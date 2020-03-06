package ru.bitc.totdesigner.system.path

/**
 * Created on 02.03.2020
 * @author YWeber */

interface PathManager {
    val externalDirLocalFile: String
    val zipType: String
        get() = "lsnx"
}