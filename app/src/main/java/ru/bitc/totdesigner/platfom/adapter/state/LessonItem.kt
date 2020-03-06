package ru.bitc.totdesigner.platfom.adapter.state

/*
 * Created on 2019-12-09
 * @author YWeber
 */
sealed class LessonItem(val hashId: String)

data class FreeCardLessonItem(
    val name: String,
    val url: String
) : LessonItem(url)

data class PaidCardLessonItem(
    val name: String,
    val url: String
) : LessonItem(url)

data class HeaderItem(
    val title: String,
    val description: String,
    val lastSearchText: String
) : LessonItem(title + lastSearchText)

data class TitleLessonItem(val title: String) : LessonItem(title)
data class ButtonLessonItem(val name: String) : LessonItem(name)

