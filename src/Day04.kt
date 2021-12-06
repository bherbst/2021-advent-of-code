private val day = "Day04"

fun main() {

    fun part1(input: List<String>): Int {
        val allDraws = input[0].split(",").map { it.toInt() }
        val cards = readCards(input)

        // Can't win before number 5!
        for (drawNumber in 5 until allDraws.size) {
            val draws = allDraws.subList(0, drawNumber)
            cards.forEach { card ->
                if (card.hasWon(draws)) {
                    return card.getScore(draws)
                }
            }
        }

        return -1
    }

    fun part2(input: List<String>): Int {
        val allDraws = input[0].split(",").map { it.toInt() }
        val cards = readCards(input).toMutableList()

        // Can't win before number 5!
        val winScores = mutableListOf<Int>()
        for (drawNumber in 5 until allDraws.size) {
            val draws = allDraws.subList(0, drawNumber)
            val markedForDeletion = mutableListOf<BingoCard>()
            cards.forEach { card ->
                if (card.hasWon(draws)) {
                    winScores += card.getScore(draws)
                    markedForDeletion += card
                }
            }
            cards.removeAll(markedForDeletion)
        }

        return winScores.last()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testResult = (part1(testInput) == 4512)
    println("test 1 passed: $testResult")

    val input = readInput(day)
    println(part1(input))

    val testResult2 = (part2(testInput) == 1924)
    println("test 2 passed: $testResult2")
    println(part2(input))
}

private fun readCards(input: List<String>): List<BingoCard> {
    return input.subList(2, input.size).windowed(size = 5, step = 6) { boardRows ->
        BingoCard(
            boardRows.map { row ->
                row.split(" ")
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
            }
        )
    }
}

private class BingoCard(
    private val rows: List<List<Int>>
) {
    fun getScore(draws: List<Int>): Int {
        val unmarkedSum = rows.sumOf { row ->
            row.sumOf { if (draws.contains(it)) 0 else it }
        }
        return unmarkedSum * draws.last()
    }

    fun hasWon(draws: List<Int>): Boolean {
        return hasHorizontalWin(draws) || hasVerticalWin(draws)
    }

    private fun hasHorizontalWin(draws: List<Int>): Boolean {
        return rows.any { row ->
            draws.containsAll(row)
        }
    }

    private fun hasVerticalWin(draws: List<Int>): Boolean {
        return (0 until 5).any { columnIndex ->
            rows.all { row -> draws.contains(row[columnIndex]) }
        }
    }
}