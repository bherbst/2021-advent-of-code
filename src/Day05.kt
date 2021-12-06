import java.lang.Integer.max
import kotlin.math.min

private val day = "Day05"

fun main() {

    fun part1(input: List<String>): Int {
        val lines = input.map { it.toLine() }.filter { it.isHorizontalOrVertical() }
        return calculateOverlaps(lines)
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { it.toLine() }
        return calculateOverlaps(lines)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 5)
    println("test 1 passed: $testResult")

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 12)
    println("test 2 passed: $testResult2")
    println(part2(input))
}

private fun calculateOverlaps(lines: List<Line>): Int {
    val gridMax = lines.getGridMax()

    var moreThanTwoOverlaps = 0
    for (y in 0..gridMax.y) {
        for (x in 0..gridMax.x) {
            val intersections = lines.count { it.intersectsWith(x, y) }
            if (intersections >= 2) moreThanTwoOverlaps++
        }
    }

    return moreThanTwoOverlaps
}

private fun List<Line>.getGridMax(): Point {
    val xMax = maxOf { it.maxX }
    val yMax = maxOf { it.maxY }

    return Point(xMax, yMax)
}

private fun String.toLine(): Line {
    val (start, end) = split(" -> ").map { it.toPoint() }
    return Line(start, end)
}

private fun String.toPoint(): Point {
    val (x, y) = split(",").map { it.toInt() }
    return Point(x, y)
}

private data class Line(
    val start: Point,
    val end: Point
) {
    val minX = min(start.x, end.x)
    val maxX = max(start.x, end.x)
    val minY = min(start.y, end.y)
    val maxY = max(start.y, end.y)

    private val lineSlope = if (minX == maxX) {
            0
        } else {
            ((start.y - end.y) / (start.x - end.x))
        }

    private val yIntercept = if (minX == maxX) {
        0
    } else {
        start.y - (lineSlope * start.x)
    }

    fun intersectsWith(x: Int, y: Int): Boolean {
        return inBounds(x, y) && onLine(x, y)
    }

    private fun inBounds(x: Int, y: Int): Boolean {
        return (x in minX..maxX) && (y in minY..maxY)
    }

    private fun onLine(x: Int, y: Int): Boolean {
        return if (maxX == minX) {
            x == maxX
        } else {
            lineSlope * x + yIntercept == y
        }
    }

    fun isHorizontalOrVertical(): Boolean {
        return maxX == minX || maxY == minY
    }
}

private data class Point(
    val x: Int,
    val y: Int
)