package ru.bitc.totdesigner.platfom.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

/**
 * Created on 24.01.2020
 * @author YWeber */

class LocalCiceroneHolder {
    private val containers: HashMap<String, Cicerone<Router>> = HashMap()

    fun cicerone(tag: String): Cicerone<Router> {
        if (!containers.containsKey(tag)) {
            containers[tag] = Cicerone.create()
        }
        return containers[tag] ?: Cicerone.create()
    }

    companion object{
        const val APP_NAVIGATION = "APP"
        const val MAIN_NAVIGATION = "MAIN"
    }
}