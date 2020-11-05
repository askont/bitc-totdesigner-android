package ru.bitc.totdesigner.platfom.adapter.setting

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_background.*
import kotlinx.android.synthetic.main.item_setting_title.*
import kotlinx.android.synthetic.main.item_status_subscription_setting.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.platfom.adapter.state.Status
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage

/**
 * Created on 05.08.2020
 * @author YWeber */

class SettingDelegateAdapter(private val clickItem: (item: SettingItem) -> Unit) {

    fun create() = ListDelegationAdapter(
        createBackgroundAdapter(clickItem),
        createTitleSetting(),
        createStatusSubscription(clickItem)
    )

    private fun createStatusSubscription(clickItem: (item: SettingItem) -> Unit) =
        adapterDelegateLayoutContainer<SettingItem.StatusSubscription, SettingItem>(R.layout.item_status_subscription_setting) {
            tvActionRecoverStatus.click { clickItem.invoke(item) }
            bind {
                tvTitleStatus.text = item.title
                tvDescriptionStatus.text = item.statusDescription
                tvActionRecoverStatus.text = item.titleButton
                tvDescriptionStatus.isVisible = item.status == Status.NOT_ACTIVE
                when (item.status) {
                    Status.ACTIVE -> viewColorBackground.isEnabled = true
                    Status.NOT_ACTIVE -> viewColorBackground.isEnabled = false
                }
            }
        }

    private fun createTitleSetting() =
        adapterDelegateLayoutContainer<SettingItem.Title, SettingItem>(R.layout.item_setting_title) {
            bind {
                tvSettingDescription.isVisible = item.description.isNotEmpty()
                tvSettingTitle.text = item.title
                tvSettingDescription.text = item.description
            }
        }

    private fun createBackgroundAdapter(clickItem: (item: SettingItem) -> Unit) =
        adapterDelegateLayoutContainer<SettingItem.BackgroundBlur, SettingItem>(R.layout.item_background) {
            itemView.click { clickItem.invoke(item) }
            bind {
                ivBackground.loadImage(item.drawableRes)
                tvTitle.text = item.title
                ivSelect.isVisible = item.checked
            }
        }

}