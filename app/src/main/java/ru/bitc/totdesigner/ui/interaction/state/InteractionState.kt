package ru.bitc.totdesigner.ui.interaction.state

import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem

/**
 * Created on 11.03.2020
 * @author YWeber */

data class InteractionState(
    val rootPicture: String,
    val partImages: List<InteractionPartItem.Part>,
    val previewImages: List<InteractionPartItem.Preview>
)