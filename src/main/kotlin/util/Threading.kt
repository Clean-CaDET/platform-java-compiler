package util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Threading {

    fun <T> iterateListSlicesViaCoroutines(
        list: List<T>,
        function: (obj: T) -> Unit,
        n: Int = optimalNumOfSlices(list)
    ) = runBlocking {

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

        indexPairs.forEach {
            launch {
                for (i in it.first..it.second)
                    function(list[i])
            }
        }
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