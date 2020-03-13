package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * Created on 12.03.2020
 * @author YWeber */


@Xml(name = "Asset")
data class Asset(
    @Path("Primitives") @Element(name = "ImagePrimitive") val primitives: ImagePrimitive,
    @PropertyElement(name = "Guid")
    val guid: String,
    @PropertyElement(name = "Name")
    val name: String,
    @PropertyElement(name = "Description")
    val description: String?,
    @PropertyElement(name = "Preview")
    val preview: String,
    @Element(name = "InitialSize") val size: InitialSize
)

@Xml(name = "ImagePrimitive")
data class ImagePrimitive(
    @PropertyElement(name = "Guid") val guid: String,
    @Element(name = "Transformation") val transformation: Transformation,
    @PropertyElement(name = "ZOrder") val zOrder: Int,
    @PropertyElement(name = "FileName") val fileName: String
)

@Xml(name = "InitialSize")
data class InitialSize(
    @PropertyElement(name = "Height") val height: Int,
    @PropertyElement(name = "Width") val width: Int
)