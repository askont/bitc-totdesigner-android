package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/*
 * Created on 2019-12-19
 * @author YWeber
 */

@Xml(name = "Lessons")
data class Lessons(@Element(name = "LessonInfo") val lessonsInfo: List<LessonInfo>)