package ru.bitc.totdesigner.model.entity

/*
 * Created on 2019-12-20
 * @author YWeber
 */
data class PreviewLessons(val previews: List<Lesson>) {

    data class Lesson(
        val title: String,
        val imageUrl: String
    )
}