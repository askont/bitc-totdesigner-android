package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/*
 * Created on 2019-12-19
 * @author YWeber
 */
@Xml(name = "LessonInfo")
data class LessonInfo(
    @Attribute(name = "name") val name: String,
    @Attribute(name = "category") val category: String,
    @PropertyElement(name = "Url") val lessonUrl: String,
    @PropertyElement(name = "Preview") val previewIcon: String
)