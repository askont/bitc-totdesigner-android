package ru.bitc.totdesigner.model.converter

import java.io.File

/**
 * Created on 04.03.2020
 * @author YWeber */

interface ConverterXmlToModel<T : Any> {
    fun convertFileToModel(file: File): T
}