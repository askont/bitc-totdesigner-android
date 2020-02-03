package ru.bitc.totdesigner.ui.splash

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.terrakok.cicerone.Navigator


/**
 * Created on 22.01.2020
 * @author YWeber */

class SplashActivity : BaseActivity(-1) {
    override val viewModel: SplashViewModel by viewModel()
    override val navigator: Navigator = ActivityNavigatorProxy(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        viewModel.delayNextScreen()
    }
}