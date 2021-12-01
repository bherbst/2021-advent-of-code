fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
            .windowed( size = 2, step = 1)
            .count { (first, second) -> second > first }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
            .windowed(size = 3, step = 1 )
            .map { it.sum() }
            .windowed(size = 2, step = 1)
            .count { (first, second) -> second > first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testResult = (part1(testInput) == 7)
    println("test passed: $testResult")

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
