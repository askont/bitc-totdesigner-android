package ru.bitc.totdesigner.platfom.converter

import java.io.File

/**
 * Created on 04.03.2020
 * @author YWeber */

interface ConverterXmlToModel<T : Any> {
    fun loadXmlToModel(file: File): T
}