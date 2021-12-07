import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

private val day = "Day07"

fun main() {

    fun part1(input: List<String>): Int {
        val crabPositions = input[0].split(",").map { it.toInt() }
        val median = crabPositions.median()
        return crabPositions.calculateFuelCost(median)
    }

    fun part2(input: List<String>): Int {
        val crabPositions = input[0].split(",").map { it.toInt() }
        val average = floor(crabPositions.average()).toInt()
        return crabPositions.calculateFuelCost2(average)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 37)
    println("test 1 passed: $testResult")

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 168)
    println("test 2 passed: $testResult2")
    println(part2(input))
}

private fun List<Int>.median(): Int {
    val midPoint: Int = size / 2
    return if (size % 2 == 0) {
        sorted()[midPoint]
    } else {
        sorted().subList(midPoint, midPoint + 1).average().toInt()
    }
}

private fun List<Int>.calculateFuelCost(destination: Int): Int {
    return fold(0) { acc, crabLocation -> acc + abs(crabLocation - destination) }
}

private fun List<Int>.calculateFuelCost2(destination: Int): Int {
    return fold(0) { acc, crabLocation ->
        val distance = abs(crabLocation - destination)
        val cost = (1..distance).sum()
        return@fold acc + cost
    }
}