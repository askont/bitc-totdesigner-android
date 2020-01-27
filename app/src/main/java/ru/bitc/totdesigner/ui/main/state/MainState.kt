package ru.bitc.totdesigner.ui.main.state

/**
 * Created on 27.01.2020
 * @author YWeber */

data class MainState (val downloadsItem: List<ItemStateLoading>,
                      val visibleDownload:Boolean = downloadsItem.isNotEmpty()) {

}