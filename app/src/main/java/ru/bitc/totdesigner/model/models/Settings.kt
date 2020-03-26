package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * Created on 12.03.2020
 * @author YWeber */

@Xml(name = "Lesson")
data class Settings(
    @PropertyElement(name = "Name") val nameLesson: String,
    @Path("Assets") @Element(name = "Asset") val assets: List<Asset>,
    @Path("Stages") @Element(name = "ConstructorStage") val stages: List<Stage>
)