package ru.bitc.totdesigner.system.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

/**
 * Created on 29.01.2020
 * @author YWeber */

interface DispatcherProvider {
    val ui:CoroutineDispatcher
    val io:CoroutineDispatcher
    val default:CoroutineDispatcher
    val unconfined:CoroutineDispatcher
}