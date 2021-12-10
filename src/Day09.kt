import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sign

private val day = "Day09"

private typealias Heightmap = List<List<Int>>

fun main() {

    fun List<String>.toHeightmap(): Heightmap {
        return map { row ->
            row.map { point -> point.digitToInt() }
        }
    }

    data class Point(
        val x: Int,
        val y: Int
    )

    fun Heightmap.getHeightAt(x: Int, y: Int): Int {
        if (y in indices) {
            val row = get(y)
            if (x in row.indices) {
                return row[x]
            }
        }

        // Otherwise we are outside the grid, so we can return max int to avoid needing to check the bounds elsewhere
        return Int.MAX_VALUE
    }
//
//    fun Heightmap.isPointInMap(point: Point): Boolean {
//        return point.y in indices && point.x in get(point.y).indices
//    }

    fun Heightmap.getAdjacentPoints(x: Int, y: Int): Map<Point, Int> {
        return mapOf(
            Point(x - 1, y) to getHeightAt(x - 1, y),
            Point(x + 1, y) to getHeightAt(x + 1, y),
            Point(x, y - 1) to getHeightAt(x, y - 1),
            Point(x, y + 1) to getHeightAt(x, y + 1)
        )
    }

    fun Heightmap.isPointLow(x: Int, y: Int): Boolean {
        val adjacentHeights = getAdjacentPoints(x, y).values
        return adjacentHeights.all { it > getHeightAt(x, y) }
    }

    fun Heightmap.getLowPoints(): Map<Point, Int> {
        val lowPoints = mutableMapOf<Point, Int>()
        forEachIndexed { y, row ->
            row.forEachIndexed { x, height ->
                if (isPointLow(x, y)) {
                    lowPoints[Point(x, y)] = height
                }
            }
        }
        println("low points: ${lowPoints.size}")
        return lowPoints
    }

    val basins = mutableMapOf<Point, Set<Point>>()
    fun Heightmap.getPointsInBasin(originPoint: Point, searchPoint: Point = originPoint): Set<Point> {
        val knownPoints = basins[originPoint] ?: emptySet()
        val includedPoints = getAdjacentPoints(searchPoint.x, searchPoint.y)
            .filter { it.key !in knownPoints && it.value < 9 }

        return if (includedPoints.isEmpty()) {
            emptySet()
        } else {
            val newKnownPoints  = knownPoints + includedPoints.keys
            basins[originPoint] = newKnownPoints
            val expandedPoints = includedPoints.map {
                    (point, _) -> getPointsInBasin(originPoint, point)
            }.flatten()
            return newKnownPoints + expandedPoints
        }
    }

    fun Heightmap.getBasins(): List<Set<Point>> {
        val lowPoints = getLowPoints()
        return lowPoints.map { (point, _) ->
            getPointsInBasin(point)
        }
    }

    fun part1(input: List<String>): Int {
        val heightmap = input.toHeightmap()
        val lowPoints = heightmap.getLowPoints()
        return lowPoints.values.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val heightmap = input.toHeightmap()
        val basins = heightmap.getBasins()
        return basins.sortedByDescending { it.size }
            .take(3)
            .fold(1) { acc, basin -> acc * basin.size}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 15)
    println("test 1 passed: $testResult - " + part1(testInput))

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 1134)
    println("test 2 passed: $testResult2")
    println(part2(input))
}