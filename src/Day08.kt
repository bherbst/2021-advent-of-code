import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sign

private val day = "Day08"

fun main() {

    fun part1(input: List<String>): Int {
        val entries = input.toPuzzleEntries()
        return entries.sumOf { entry -> entry.outputSegments.countUniqueDigits() }
    }

    fun part2(input: List<String>): Int {
        val entries = input.toPuzzleEntries()
        return entries.sumOf { entry ->
            val digitSignalMap = entry.signalPatterns.buildWireDigitMap()
            entry.getDisplayedDigits(digitSignalMap)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 26)
    println("test 1 passed: $testResult - " + part1(testInput))

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 61229)
    println("test 2 passed: $testResult2")
    println(part2(input))
}

private fun List<String>.toPuzzleEntries(): List<Entry> {
    return map { line ->
        val (combinedSignalPatterns, combinedOutputSegments) = line.split(" | ")
        Entry(
            signalPatterns = combinedSignalPatterns.split(" ").map { it.toCharSet() },
            outputSegments = combinedOutputSegments.split(" " ).map { it.toCharSet() }
        )
    }
}

private val allSegments = setOf('a', 'b', 'c', 'd', 'e', 'f', 'g')

// Actual digit to number of segments
private val digitSegmentCount = mapOf(
    0 to 6,
    1 to 2,
    2 to 5,
    3 to 5,
    4 to 4,
    5 to 5,
    6 to 6,
    7 to 3,
    8 to 7,
    9 to 6
)

private val uniqueDigits = setOf(1, 4, 7, 8)

private val uniqueDigitSegmentCounts = digitSegmentCount.filter { (key, _) -> key in uniqueDigits }

private fun List<Set<Char>>.countUniqueDigits(): Int {
    return count { digit ->
        digit.size in uniqueDigitSegmentCounts.values
    }
}

private fun List<Set<Char>>.buildWireDigitMap(): Map<Int, Set<Char>> {
    val remainingSignals = toMutableList()
    val map = mutableMapOf<Int, Set<Char>>()

    // Start with the easy digits
    uniqueDigits.forEach { digit ->
        val wires = findWire(digitSegmentCount[digit]!!)
        map[digit] = wires
    }
    remainingSignals.removeAll(map.values)

    // We have 1, 4, 7, 8 now.
    // 6 segments = 0, 6, 9
    // "9" is the 6 segment digit completely containing the segments in "4"
    map[9] = remainingSignals.findWire(6, containingSegments = map[4]!!)
    remainingSignals.remove(map[9])

    // "0" is the 6 segment digit completely containing the segments in "7"
    map[0] = remainingSignals.findWire(6, containingSegments = map[7]!!)
    remainingSignals.remove(map[0])

    // "6" is the last 6 segment digit
    map[6] = remainingSignals.findWire(6)
    remainingSignals.remove(map[6])


    // 5 segments = 2, 3, 5
    // "3" is the 5 segment digit completely containing the segments in "1"
    map[3] = remainingSignals.findWire(5, containingSegments = map[1]!!)
    remainingSignals.remove(map[3])

    // "5" is the 5 segment digit completely contained within a "6"
    map[5] = remainingSignals.findWire(5, containedWithinSegments = map[6]!!)
    remainingSignals.remove(map[5])

    // "2" is the last digit
    map[2] = remainingSignals[0]

    return map
}

private fun List<Set<Char>>.findWire(
    numSegments: Int,
    containingSegments: Set<Char> = emptySet(),
    containedWithinSegments: Set<Char> = allSegments
): Set<Char>  {
    return find { signals ->
        signals.size == numSegments
                && signals.containsAll(containingSegments)
                && containedWithinSegments.containsAll(signals)
    }!!
}

private fun String.toCharSet() = toList().toSet()

private class Entry(
    val signalPatterns: List<Set<Char>>,
    val outputSegments: List<Set<Char>>
) {
    fun getDisplayedDigits(digitSignalMap: Map<Int, Set<Char>>): Int {
        return outputSegments.joinToString("") { segments ->
            digitSignalMap.entries.find { (_, signals) -> signals == segments }!!.key.toString()
        }.toInt()
    }
}