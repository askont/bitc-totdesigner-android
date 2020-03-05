package ru.bitc.totdesigner.model.models

import com.tickaroo.tikxml.annotation.*

/**
 * Created on 04.03.2020
 * @author YWeber */

@Xml(name = "LessonPreview")
data class SavedLessonPreview(
    @PropertyElement(name = "Name") val nameLesson: String,
    @PropertyElement(name = "Authors") val authors: String?,
    @Path("Tags") @Element(name = "string") val tags: List<TagString>?,
    @PropertyElement(name = "Description") val description: String,
    @PropertyElement(name = "MainPreview") val mainImage: String,
    @PropertyElement(name = "ResourceHash") val hash: String,
    @Path("Previews") @Element(name = "string")
    val images: List<ImageString>
) {

    @Xml
    data class TagString(@TextContent val tag: String)

    @Xml
    data class ImageString(@TextContent val nameImage: String)
}