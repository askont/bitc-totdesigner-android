package ru.bitc.totdesigner.ui

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.terrakok.cicerone.Navigator

class AppActivity : BaseActivity(R.layout.activity_main) {

    override val viewModel: AppViewModel by viewModel()
    override val navigator: Navigator
        get() = ActivityNavigatorProxy(this, R.id.appContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setMainFlowScreen()
    }

}
