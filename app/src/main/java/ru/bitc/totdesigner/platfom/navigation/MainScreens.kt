package ru.bitc.totdesigner.platfom.navigation

import androidx.fragment.app.Fragment
import ru.bitc.totdesigner.ui.catalog.CatalogFragment
import ru.bitc.totdesigner.ui.home.HomeFragment
import ru.bitc.totdesigner.ui.main.MainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Created on 22.01.2020
 * @author YWeber */

class MainScreens {

    object HomeScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return HomeFragment.newInstance()
        }
    }

    object CatalogScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return CatalogFragment.newInstance()
        }
    }

}