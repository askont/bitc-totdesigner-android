package ru.bitc.totdesigner.platfom.adapter.state

/**
 * Created on 11.03.2020
 * @author YWeber */

sealed class InteractionPartItem(open val id: String) {

    data class Part(
        override val id: String,
        val path: String,
        val name: String,
        val positionX: Int = 0,
        val positionY: Int = 0,
        val height: Int = 0,
        val width: Int = 0,
        val isPermissionDrop: Boolean
    ) : InteractionPartItem(id)

    data class Preview(
        val path: String,
        val position: Int,
        val isSelect: Boolean = false
    ) : InteractionPartItem(path)
}

