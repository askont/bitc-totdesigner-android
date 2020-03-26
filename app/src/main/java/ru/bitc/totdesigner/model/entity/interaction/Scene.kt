package ru.bitc.totdesigner.model.entity.interaction

/**
 * Created on 19.03.2020
 * @author YWeber */

data class Scene(
    val position: Int,
    val description: String,
    val previewImagePath: String,
    val partImages: List<PartImage>
)