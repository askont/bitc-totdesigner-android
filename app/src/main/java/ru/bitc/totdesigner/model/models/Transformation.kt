package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * Created on 12.03.2020
 * @author YWeber */


@Xml(name = "Transformation")
data class Transformation(
    @PropertyElement(name = "M11") val m11: Int,
    @PropertyElement(name = "M12") val m12: Int,
    @PropertyElement(name = "M13") val m13: Int,
    @PropertyElement(name = "M21") val m21: Int,
    @PropertyElement(name = "M22") val m22: Int,
    @PropertyElement(name = "M23") val m23: Int
)