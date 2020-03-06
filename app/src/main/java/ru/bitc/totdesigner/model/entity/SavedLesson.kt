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
) {
    companion object {
        fun defaultSavedLesson(
            localPathLesson: String = "",
            remotePath: String = "",
            nameLesson: String = "",
            descriptionLesson: String = "",
            mainImage: String = "",
            otherImage: List<String> = listOf()
        ) = SavedLesson(
            localPathLesson,
            remotePath,
            nameLesson,
            descriptionLesson,
            mainImage,
            otherImage
        )
    }
}