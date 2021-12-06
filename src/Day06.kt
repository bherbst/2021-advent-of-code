import java.lang.Integer.max
import kotlin.math.min

private val day = "Day06"

fun main() {

    fun part1(input: List<String>): Long {
        var fish = input[0].generateFish()
        repeat(80) {
            fish = fish.nextDay()
        }

        return fish.fishCount()
    }

    fun part2(input: List<String>): Long {
        var fish = input[0].generateFish()
        repeat(256) {
            fish = fish.nextDay()
        }

        return fish.fishCount()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 5934L)
    println("test 1 passed: $testResult")

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 26984457539)
    println("test 2 passed: $testResult2")
    println(part2(input))
}

private fun String.generateFish(): Map<Int, Long> {
    val map = mutableMapOf<Int, Long>()
    (0..8).forEach { map[it] = 0 }
    split(",")
        .map { it.toInt() }
        .forEach { timer ->
            map[timer] = map[timer]!! + 1
        }
    return map
}


private fun Map<Int, Long>.nextDay(): Map<Int, Long> {
    val updatedFish = mutableMapOf<Int, Long>()
    forEach { (timer, count) ->
        if (timer == 0) {
            updatedFish[8] = count
            updatedFish[6] = (updatedFish[6] ?: 0) + count
        } else {
            updatedFish[timer - 1] = (updatedFish[timer - 1] ?: 0) + count
        }
    }
    return updatedFish
}

private fun Map<Int, Long>.fishCount(): Long = toList().sumOf { (_, count) -> count }