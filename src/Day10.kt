private val day = "Day10"

private sealed class ParseResult {
    data class Valid(val chunks: List<Chunk>): ParseResult()
    data class Corrupted(val invalidSymbol: Char): ParseResult()
    data class Incomplete(val line: String, val missingChar: Char): ParseResult()
}

private class Chunk(
    val startSymbol: Char,
    val chunks: List<Chunk>
){
    fun charLength(): Int = 2 + chunks.sumOf { it.charLength() }
}

fun main() {

    class ChunkSyntaxException(
        val invalidSymbol: Char
    ) : Exception()

    class IncompleteLineException(
        val missingSymbol: Char
    ): Exception()

    val startingSymbols = setOf(
        '(',
        '[',
        '{',
        '<'
    )

    fun Char.corruptionPoints(): Int {
        return when (this) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

    fun Char.incompletePoints(): Int {
        return when (this) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> 0
        }
    }

    fun Char.endSymbol(): Char = when(this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw Exception("Unknown start symbol $this")
    }

    fun checkSymbolMatch(startSymbol: Char, endSymbol: Char): Boolean {
        return endSymbol == startSymbol.endSymbol()
    }

    fun String.readChunks(): List<Chunk> {
        var startSymbolIndex = 0
        var startSymbol = this[startSymbolIndex]
        val chunks = mutableListOf<Chunk>()

        while (startSymbol in startingSymbols) {
            if (startSymbolIndex + 1 >= length) throw IncompleteLineException(missingSymbol = startSymbol.endSymbol())
            val subChunks = substring(startSymbolIndex + 1, length).readChunks()
            val chunk = Chunk(startSymbol, subChunks)

            val endSymbolIndex = startSymbolIndex + chunk.charLength() - 1
            if (endSymbolIndex >= length) throw IncompleteLineException(missingSymbol = startSymbol.endSymbol())
            val endSymbol = this[endSymbolIndex]
            if (checkSymbolMatch(startSymbol, endSymbol)) {
                chunks += chunk
            } else {
                throw ChunkSyntaxException(endSymbol)
            }

            startSymbolIndex = endSymbolIndex + 1
            if (startSymbolIndex >= length) return chunks
            startSymbol = this[startSymbolIndex]
        }

        return chunks
    }

    fun String.parseLine(): ParseResult {
        return try {
            ParseResult.Valid(readChunks())
        } catch (e: ChunkSyntaxException) {
            ParseResult.Corrupted(e.invalidSymbol)
        } catch (e: IncompleteLineException) {
            ParseResult.Incomplete(this, e.missingSymbol)
        }
    }

    fun ParseResult.Incomplete.repair() = line + missingChar

    fun ParseResult.Incomplete.calculateRepairScore(): Long {
        var result: ParseResult = this
        var score = 0L
        while (result is ParseResult.Incomplete) {
            score = (score * 5) + result.missingChar.incompletePoints()
            result = result.repair().parseLine()
        }
        return score
    }

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