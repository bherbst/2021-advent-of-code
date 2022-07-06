private val day = "Day11"

fun main() {

    fun part1(input: List<String>): Int {
        val parseResults = input.map { it.parseLine() }
        return parseResults.filterIsInstance<ParseResult.Corrupted>()
            .sumOf { it.invalidSymbol.corruptionPoints() }
    }

    fun part2(input: List<String>): Long {
        val scores = input.map { it.parseLine() }
            .filterIsInstance<ParseResult.Incomplete>()
            .map { it.calculateRepairScore() }
            .sorted()
        println(scores)
        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 26397)
    println("test 1 passed: $testResult - " + part1(testInput))

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 288957L)
    println("test 2 passed: $testResult2")
    println(part2(input))
}