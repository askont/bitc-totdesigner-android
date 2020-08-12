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

class SettingDelegateAdapter(private val clickItem: (item: SettingItem) -> Unit) {

    fun create() = ListDelegationAdapter<List<SettingItem>>(
        createBackgroundAdapter(clickItem)
    )

    private fun createBackgroundAdapter(clickItem: (item: SettingItem) -> Unit) =
        adapterDelegateLayoutContainer<SettingItem, SettingItem>(R.layout.item_background) {
            itemView.click { clickItem.invoke(item) }
            bind {
                ivBackground.loadImage(item.drawableRes)
                tvTitle.text = item.title
                ivSelect.isVisible = item.checked
            }
        }

}