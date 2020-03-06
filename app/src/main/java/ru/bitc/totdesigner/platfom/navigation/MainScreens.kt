package ru.bitc.totdesigner.platfom.navigation

import androidx.fragment.app.Fragment
import ru.bitc.totdesigner.ui.catalog.CatalogFragment
import ru.bitc.totdesigner.ui.catalog.dialog.DownloadDialog
import ru.bitc.totdesigner.ui.home.HomeFragment
import ru.bitc.totdesigner.ui.home.dialog.DetailedLessonDialog
import ru.bitc.totdesigner.ui.loading.LoadingDetailedFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Created on 22.01.2020
 * @author YWeber */

class MainScreens {


    data class FreeDownloadDialogScreen(private val nameQuest: String) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return DownloadDialog.newInstance(nameQuest)
        }
    }

    data class DetailedLessonDialogScreen(val remotePath: String) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return DetailedLessonDialog.newInstance(remotePath)
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

    object LoadingDetailedScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return LoadingDetailedFragment.newInstance()
        }
    }

}