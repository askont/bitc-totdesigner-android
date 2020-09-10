package ru.bitc.totdesigner.model.entity

/**
 * Created on 09.09.2020
 * @author YWeber */

sealed class LessonCatalogInteractive {
    class Find(val id: String) : LessonCatalogInteractive()
    object Empty : LessonCatalogInteractive()
}