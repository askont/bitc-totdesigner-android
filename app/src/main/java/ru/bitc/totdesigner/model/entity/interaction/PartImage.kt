package ru.bitc.totdesigner.model.entity.interaction

/**
 * Created on 19.03.2020
 * @author YWeber */

data class PartImage(
    val guid: String,
    val pathImage: String,
    val namePart: String,
    val isStatic: Boolean,
    val positionX: Int = 0,
    val positionY: Int = 0,
    val height: Int = 0,
    val width: Int = 0
)