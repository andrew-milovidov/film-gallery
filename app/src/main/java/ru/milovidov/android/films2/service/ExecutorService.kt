package ru.milovidov.android.films2.service

import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ExecutorService {
    companion object {
        private val CORE_POOL_SIZE = 3
        private val MAX_POOL_SIZE = 10
        private val KEEP_ALIVE_TIME = 500L

        val workQueue = LinkedBlockingQueue<Runnable>()

        val threadPoolExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS, workQueue
        )

        fun execute(task : () -> Unit) {
            threadPoolExecutor.execute(Runnable(task))
        }

        fun <F> executeWithFuture (task : () -> F) : Future<F> {
            return threadPoolExecutor.submit(task)
        }
    }

}