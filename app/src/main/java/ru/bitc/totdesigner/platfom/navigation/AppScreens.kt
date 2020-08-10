package ru.bitc.totdesigner.platfom.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.bitc.totdesigner.ui.AppActivity
import ru.bitc.totdesigner.ui.interaction.InteractionActivity
import ru.bitc.totdesigner.ui.main.MainFragment
import ru.bitc.totdesigner.ui.mock.MockFragment
import ru.bitc.totdesigner.ui.setting.SettingFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Created on 21.01.2020
 * @author YWeber */

object AppScreens {
    object MainFlowScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return MainFragment.newInstance()
        }
    }

    object AppFlowScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, AppActivity::class.java)
        }
    }

    data class InteractionRootScreen(val remoteUrl: String) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent {
            return InteractionActivity.newInstance(remoteUrl, context)
        }
    }

    data class MockScreen(private val mockMessage: String) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return MockFragment.newInstance(mockMessage)
        }
    }

    object SettingScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return SettingFragment.newInstance()
        }
    }

}