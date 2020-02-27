package ru.bitc.totdesigner.system.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Created on 26.02.2020
 * @author YWeber */

class CoroutineTestRule(private val dispatcherProvider: TestDispatcher = TestDispatcher()) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher = dispatcherProvider.testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        dispatcherProvider.testDispatcher.cleanupTestCoroutines()
    }

    fun test(block: suspend () -> Unit) {
        dispatcherProvider.testDispatcher.runBlockingTest {
            block()
        }
    }
}