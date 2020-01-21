package com.andigeeky.mvpapp.ui.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
@Singleton
open class AppExecutors(
    val io: Executor,
    val default : Executor,
    val mainThread: Executor
) {
    @Inject
    constructor() : this(
        DispatcherExecutor(Dispatchers.IO),
        DispatcherExecutor(Dispatchers.Default),
        DispatcherExecutor(Dispatchers.Main)
    )
}

class DispatcherExecutor(private val dispatcher : CoroutineDispatcher) : Executor {
    override fun execute(command: Runnable) {
        dispatcher.dispatch(EmptyCoroutineContext, command)
    }
}
