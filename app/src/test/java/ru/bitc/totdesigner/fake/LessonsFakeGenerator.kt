package ru.bitc.totdesigner.fake

import ru.bitc.totdesigner.platfom.converter.ModelLessonToEntityPreviewConverter
import ru.bitc.totdesigner.model.models.Lessons

/**
 * Created on 17.02.2020
 * @author YWeber */
val lessonInfoFake = Lessons.LessonInfo(
    "Вирус ВИЧ. Риски",
    "Free",
    "http://test/package",
    "https://totdesigner.s3.eu-central-1.amazonaws.com/previews/OrigamiDeer.png"
)

val lessonsFake = listOf(
    lessonInfoFake,
    Lessons.LessonInfo(
        "Квест-оригами Олень",
        "Free",
        "https://totdesigner.s3.eu-central-1.amazonaws.com/lessons/%D0%9E%D0%BB%D0%B5%D0%BD%D1%8C+%D0%BE%D1%80%D0%B8%D0%B3%D0%B0%D0%BC%D0%B8.lsnx",
        "https://totdesigner.s3.eu-central-1.amazonaws.com/previews/OrigamiDeer.png"
    ),
    Lessons.LessonInfo(
        "Квест-оригами Лев",
        "Free",
        "https://totdesigner.s3.eu-central-1.amazonaws.com/lessons/%D0%9B%D0%B5%D0%B2+%D0%BE%D1%80%D0%B8%D0%B3%D0%B0%D0%BC%D0%B8.lsnx",
        "https://totdesigner.s3.eu-central-1.amazonaws.com/previews/OrigamiLion.png"
    ),
    Lessons.LessonInfo(
        "Квест-оригами Жираф",
        "Free",
        "https://s3.eu-central-1.amazonaws.com/totdesigner/lessons/%D0%A1%D1%82%D1%80%D0%B5%D0%BB%D0%BE%D1%83%D1%85+%D0%BE%D1%80%D0%B8%D0%B3%D0%B0%D0%BC%D0%B8.lsnx",
        "https://s3.eu-central-1.amazonaws.com/totdesigner/previews/OrigamiGalago.png"
    ),
    Lessons.LessonInfo(
        "Квест-оригами Крокодил",
        "Paid",
        "https://s3.eu-central-1.amazonaws.com/totdesigner/lessons/%D0%A1%D1%82%D1%80%D0%B5%D0%BB%D0%BE%D1%83%D1%85+%D0%BE%D1%80%D0%B8%D0%B3%D0%B0%D0%BC%D0%B8.lsnx",
        "https://s3.eu-central-1.amazonaws.com/totdesigner/previews/OrigamiGalago.png"
    ),
    Lessons.LessonInfo(
        "Квест-оригами Стрелоух",
        "Paid",
        "https://s3.eu-central-1.amazonaws.com/totdesigner/lessons/%D0%A1%D1%82%D1%80%D0%B5%D0%BB%D0%BE%D1%83%D1%85+%D0%BE%D1%80%D0%B8%D0%B3%D0%B0%D0%BC%D0%B8.lsnx",
        "https://s3.eu-central-1.amazonaws.com/totdesigner/previews/OrigamiGalago.png"
    )
)

val previewLesson = ModelLessonToEntityPreviewConverter().convertModelInfoToLesson(lessonInfoFake)
