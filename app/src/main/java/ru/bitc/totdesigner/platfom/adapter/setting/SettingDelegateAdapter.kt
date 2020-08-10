package ru.bitc.totdesigner.platfom.adapter.setting

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_background.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage

/**
 * Created on 05.08.2020
 * @author YWeber */

class SettingDelegateAdapter {

    fun create() = ListDelegationAdapter<List<SettingItem>>(
        createBackgroundAdapter()
    )

    private fun createBackgroundAdapter() =
        adapterDelegateLayoutContainer<SettingItem, SettingItem>(R.layout.item_background) {
            itemView.click {  }
            bind {
                ivBackground.loadImage(item.drawableRes)
                tvTitle.text = item.title
                ivSelect.isVisible = item.checked
            }
        }

}