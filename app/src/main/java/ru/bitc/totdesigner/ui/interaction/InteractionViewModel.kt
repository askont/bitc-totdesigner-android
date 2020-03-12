package ru.bitc.totdesigner.ui.interaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.ui.interaction.state.InteractionState

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionViewModel : BaseViewModel() {

    private val action = MutableLiveData<InteractionState>()

    private val currentState
        get() = action.value ?: InteractionState("", listOf(), listOf())

    val viewState: LiveData<InteractionState>
        get() = action

    init {
        val previewImages = mutableListOf<InteractionPartItem.Preview>()
        previewImages.add(
            InteractionPartItem.Preview(
                "https://bipbap.ru/wp-content/uploads/2017/10/0_8eb56_842bba74_XL-640x400.jpg",
                true
            )
        )
        previewImages.add(InteractionPartItem.Preview("https://2krota.ru/wp-content/uploads/2019/02/0_i-1.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://bipbap.ru/wp-content/uploads/2017/04/72fqw2qq3kxh.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://klike.net/uploads/posts/2019-05/1559113982_1.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://i.pinimg.com/originals/c0/b7/7f/c0b77faeb2cb3e67fd1b423c4535f6c3.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://ribalych.ru/wp-content/uploads/2019/10/smeshnye-kartinki-s-nadpis_001.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://i.uaportal.com/2019/3/1/3.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://bestcube.space/wp-content/uploads/0Jrvgf38V8.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://neumeka.ru/images/stat/int/skachat_kartinki/04.jpg"))
        previewImages.add(InteractionPartItem.Preview("https://images.wallpaperscraft.ru/image/siluet_luna_lodka_135277_300x168.jpg"))
        val partImages = mutableListOf<InteractionPartItem.Part>()
        for (i in 0..100) {
            partImages.add(
                InteractionPartItem.Part(
                    "https://images.aif.ru/007/410/7099f160be2419d6128e3a57e62ec326.jpg",
                    "описания $i"
                )
            )
        }
        action.value = currentState.copy(
            "https://bestcube.space/wp-content/uploads/0Jrvgf38V8.jpg",
            partImages, previewImages
        )

    }

}