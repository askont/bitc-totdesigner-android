package ru.bitc.totdesigner.platfom.navigation

import androidx.fragment.app.Fragment
import ru.bitc.totdesigner.ui.catalog.CatalogFragment
import ru.bitc.totdesigner.ui.catalog.dialog.FreeDownloadDialog
import ru.bitc.totdesigner.ui.home.HomeFragment
import ru.bitc.totdesigner.ui.loading.AllLoadingFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Created on 22.01.2020
 * @author YWeber */

class MainScreens {


    data class FreeDownloadDialogScreen(private val nameQuest: String) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return FreeDownloadDialog.newInstance(nameQuest)
        }
    }

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

    object AllLoadingScreen : SupportAppScreen(){
        override fun getFragment(): Fragment {
            return AllLoadingFragment.newInstance()
        }
    }

}