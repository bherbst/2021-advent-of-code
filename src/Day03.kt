fun main() {
    fun part1(input: List<String>): Int {
        val gammaRateStr = (0 until input[0].length).map { position ->
            input.getMostCommonBitAt(position)
        }.joinToString(separator = "")
        val gammaRate = Integer.parseInt(gammaRateStr, 2)

        val epsilonRateStr = gammaRateStr.invertBitString()
        val epsilonRate = Integer.parseInt(epsilonRateStr, 2)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        val oxygenRatingStr = input.findRating { list, position -> list.getMostCommonBitAt(position) }
        val co2RatingStr = input.findRating { list, position -> list.getLeastCommonBitAt(position) }

        val oxygenRating = Integer.parseInt(oxygenRatingStr, 2)
        val co2Rating = Integer.parseInt(co2RatingStr, 2)

        println("oxy: $oxygenRatingStr ($oxygenRating)")
        println("c02: $co2RatingStr ($co2Rating)")

        return oxygenRating * co2Rating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val testResult = (part1(testInput) == 198)
    println("test passed: $testResult")

    val input = readInput("Day03")
    println(part1(input))
//
    val testResult2 = (part2(testInput) == 230)
    println("test2 passed: $testResult2")
    println(part2(input))
}

private fun List<String>.getMostCommonBitAt(position: Int): Char {
    val ones = count { it[position] == '1' }
    val zeros = count { it[position] == '0' }

    return if (ones >= zeros) '1' else '0'
}

private fun List<String>.getLeastCommonBitAt(position: Int): Char {
    val mcb = getMostCommonBitAt(position)
    return if (mcb == '1') '0' else '1'
}

private fun String.invertBitString(): String {
    return map { if (it == '0') '1' else '0' }.joinToString(separator = "")
}

private fun List<String>.findRating(criteria: (List<String>, Int) -> Char): String {
    var haystack = toList()
    var position = 0
    while (haystack.size > 1) {
        val checkBit = criteria(haystack, position)
        haystack = haystack.filter { num -> num[position] == checkBit }
        position++
    }

    return haystack[0]
}