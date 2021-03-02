package ru.bitc.totdesigner.ui.setting.state

import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.model.prefs.PrefsStore
import ru.bitc.totdesigner.platfom.adapter.state.PriceSubscription
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.platfom.adapter.state.Status
import ru.bitc.totdesigner.system.ResourceManager

class SettingPlatform(prefsStore: PrefsStore, resourceManager: ResourceManager) {
    val createBackgroundBlurItems = listOf(
        SettingItem.Title(resourceManager.getString(R.string.handling_subscription_title), ""),
        SettingItem.StatusSubscription(
            resourceManager.getString(R.string.activie_status_subscription_title),
            Status.NOT_ACTIVE,
            resourceManager.getString(R.string.description_status_subscription_not_active),
            resourceManager.getString(R.string.action_recover_subscription_status)
        ),
        SettingItem.AvailableSubscriptions(
            "Доступные подписки", listOf(
                PriceSubscription("Годовая возобновляемая, 12 мес.", "3500 ₽"),
                PriceSubscription("Годовая, 12 мес.", "4000 ₽"),
                PriceSubscription("Месячная, 30/31 день", "549 ₽"),
                PriceSubscription("Недельная, 7 дней", "199 ₽"),
            )
        ),
        SettingItem.Title(resourceManager.getString(R.string.design_title), ""),
        SettingItem.BackgroundBlur(
            R.drawable.img_totdesign,
            resourceManager.getString(R.string.totdesigner),
            prefsStore.currentBackground == R.drawable.img_totdesign
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_world,
            resourceManager.getString(R.string.home_title),
            prefsStore.currentBackground == R.drawable.img_world
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_red_planet,
            resourceManager.getString(R.string.red_planet_title),
            prefsStore.currentBackground == R.drawable.img_red_planet
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_ice,
            resourceManager.getString(R.string.ice_title),
            prefsStore.currentBackground == R.drawable.img_ice
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_business,
            resourceManager.getString(R.string.business),
            prefsStore.currentBackground == R.drawable.img_business
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_office,
            resourceManager.getString(R.string.office),
            prefsStore.currentBackground == R.drawable.img_office
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_yellow_flower,
            resourceManager.getString(R.string.yellow_flower),
            prefsStore.currentBackground == R.drawable.img_yellow_flower
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_blue_flower,
            resourceManager.getString(R.string.blue_flower),
            prefsStore.currentBackground == R.drawable.img_blue_flower
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_spring,
            resourceManager.getString(R.string.spring),
            prefsStore.currentBackground == R.drawable.img_spring
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_summer,
            resourceManager.getString(R.string.summer),
            prefsStore.currentBackground == R.drawable.img_summer
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_autumn,
            resourceManager.getString(R.string.autumn),
            prefsStore.currentBackground == R.drawable.img_autumn
        ),
        SettingItem.BackgroundBlur(
            R.drawable.img_black_and_white,
            resourceManager.getString(R.string.black_and_white),
            prefsStore.currentBackground == R.drawable.img_black_and_white
        )
    )
}