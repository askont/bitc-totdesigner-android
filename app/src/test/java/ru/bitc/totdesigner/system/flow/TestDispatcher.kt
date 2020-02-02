package ru.bitc.totdesigner.system.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcher : DispatcherProvider {
    override val ui: CoroutineDispatcher = Dispatchers.Unconfined
    override val io: CoroutineDispatcher = Dispatchers.Unconfined
    override val default: CoroutineDispatcher = Dispatchers.Unconfined
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}