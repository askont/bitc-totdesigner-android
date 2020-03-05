package ru.bitc.totdesigner.model.entity

/**
 * Created on 04.03.2020
 * @author YWeber */

data class SavedLesson(
    val localPathLesson: String,
    val remotePath: String,
    val nameLesson: String,
    val descriptionLesson: String,
    val mainImage: String,
    val otherImage: List<String>
)