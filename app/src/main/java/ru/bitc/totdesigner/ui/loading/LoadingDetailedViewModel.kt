package ru.bitc.totdesigner.ui.loading

import ru.bitc.totdesigner.platfom.BaseViewModel
import ru.bitc.totdesigner.platfom.navigation.AppScreens
import ru.bitc.totdesigner.platfom.navigation.MainScreens
import ru.terrakok.cicerone.Router

/**
 * Created on 04.02.2020
 * @author YWeber */

class LoadingDetailedViewModel(private val mainRouter: Router) : BaseViewModel() {
    fun backTo() {
        mainRouter.exit()
    }
}