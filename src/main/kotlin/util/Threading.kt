package util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

object Threading {

    class UniqueLock {
        var inUse = false
        private fun lock() { inUse = true }
        private fun unlock() { inUse = false }

        fun runThreadSafe(function: () -> Unit) {
            while (inUse)
                Thread.sleep(0, (Math.random() * 10).toInt())
            lock()
            function()
            unlock()
        }
    }

    fun <T> iterateListSlicesViaThreads(
        list: List<T>,
        function: (obj: T) -> Unit,
        n: Int = optimalNumOfSlices(list)
    ) {
        if (list.isEmpty()) return
        val threads = mutableListOf<Thread>()

        calculateIndexPairs(list, n).forEach {
            threads.add(
                thread(start = true) {
                for (i in it.first..it.second)
                    function(list[i])
            })
        }

        threads.forEach { it.join() }
    }

    fun <T> iterateListSlicesViaCoroutines(
        list: List<T>,
        function: (obj: T) -> Unit,
        n: Int = optimalNumOfSlices(list)
    ) = runBlocking {

        calculateIndexPairs(list, n).forEach {
            launch {
                for (i in it.first..it.second)
                    function(list[i])
            }
        }
    }

    private fun <T> calculateIndexPairs(list: List<T>, n: Int): List<Pair<Int, Int>> {
        if (list.size < n) error("Cannot split list into sections, list does not have enough elements.")
        val indexPairs = mutableListOf<Pair<Int, Int>>()

        val split = list.size / n

        var iterator = 0
        while (iterator < list.size) {
            val end =
                if (iterator + split < list.size) iterator + split - 1
                else list.size - 1
            indexPairs.add(Pair(iterator, end))
            iterator += split
        }

        return indexPairs
    }

    private fun <T> optimalNumOfSlices(list: List<T>): Int {
        val size = list.size

        return when {
            size > 10000 -> 20
            size > 1000 -> 10
            size > 500 -> 5
            size > 100 -> 3
            else -> 2
        }
    }
}