package ru.bitc.totdesigner.platfom.adapter.state

/**
 * Created on 11.03.2020
 * @author YWeber */

sealed class InteractionPartItem {

    data class Part(
        val path: String,
        val name: String
    ) : InteractionPartItem()

    data class Preview(
        val path: String,
        val isSelect: Boolean = false
    ) : InteractionPartItem()
}

