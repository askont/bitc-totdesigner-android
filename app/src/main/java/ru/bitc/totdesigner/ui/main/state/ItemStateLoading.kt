package ru.bitc.totdesigner.ui.main.state

/**
 * Created on 27.01.2020
 * @author YWeber */

sealed class ItemStateLoading

data class LoadingItem(val progress: Int, val message: String) : ItemStateLoading()