package com.govi.todoapp

import com.govi.todoapp.core.coroutines.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    scheduler: TestCoroutineScheduler = TestCoroutineScheduler()
) : TestWatcher() {
    val testDispatcher = StandardTestDispatcher(scheduler)

    val dispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
        override val unconfined = testDispatcher
    }
    private lateinit var closeable: AutoCloseable

    override fun starting(description: Description) {
        super.starting(description)
        closeable = MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        closeable.close()
        Dispatchers.resetMain()
    }
}