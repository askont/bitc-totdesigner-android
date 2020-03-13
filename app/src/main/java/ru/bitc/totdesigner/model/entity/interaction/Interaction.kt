package ru.bitc.totdesigner.model.entity.interaction

/**
 * Created on 13.03.2020
 * @author YWeber */

data class Interaction(
    val partImages: List<Part>,
    val previews: List<Preview>
) {
    data class Part(
        val pathImage: String,
        val namePart: String
    )

    data class Preview(
        val pathImage: String,
        val position: Int
    )
}