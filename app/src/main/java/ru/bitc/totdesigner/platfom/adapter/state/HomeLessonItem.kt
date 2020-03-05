package ru.bitc.totdesigner.platfom.adapter.state

/**
 * Created on 04.03.2020
 * @author YWeber */

sealed class HomeLessonItem(val id: String) {

}

data class SavedHomeLessonItem(
    val lessonUrl: String,
    val nameLesson: String,
    val mainImageLocalPath: String
) : HomeLessonItem(lessonUrl)

data class BottomHomeLessonItem(val name: String) : HomeLessonItem(name)

data class TitleHomeLessonItem(val title: String) : HomeLessonItem(title)
data class HeaderHomeLesson(
    val title: String,
    val description: String
) : HomeLessonItem(title + description)
