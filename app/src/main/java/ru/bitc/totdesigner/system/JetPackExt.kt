package ru.bitc.totdesigner.system

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/*
 * Created on 2019-12-09
 * @author YWeber
 */

fun <T : Any, L : LiveData<T>> Fragment.subscribe(liveData: L, block: (T) -> Unit) =
    liveData.observe(
        viewLifecycleOwner,
        Observer(block)
    )

fun <T : Any, L : LiveData<T>> AppCompatActivity.subscribe(liveData: L, block: (T) -> Unit) =
    liveData.observe(
        this,
        Observer(block)
    )