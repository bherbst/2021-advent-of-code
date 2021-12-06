fun main() {
    fun part1(input: List<String>): Int {
        val sub = Sub1()
        input.forEach { executeCommand(it, sub) }

        return sub.depth * sub.horizontal
    }

    fun part2(input: List<String>): Int {
        val sub = Sub2()
        input.forEach { executeCommand(it, sub) }

        return sub.depth * sub.horizontal
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val testResult = (part1(testInput) == 150)
    println("test passed: $testResult")

    val input = readInput("Day02")
    println(part1(input))


    val testResult2 = (part2(testInput) == 900)
    println("test2 passed: $testResult2")
    println(part2(input))
}

private fun executeCommand(command: String, sub: Submarine) {
    val (direction, amountStr) = command.split(" ")
    val amount = amountStr.toInt()
    when (direction) {
        "forward" -> sub.forward(amount)
        "up" -> sub.up(amount)
        "down" -> sub.down(amount)
    }
}

private interface Submarine {
    fun down(amount: Int)
    fun up(amount: Int)
    fun forward(amount: Int)
}

private class Sub1 : Submarine {
    var horizontal: Int = 0
        private set

    var depth: Int = 0
        private set

    override fun down(amount: Int) {
        depth += amount
    }

    override fun up(amount: Int) {
        depth -= amount
    }

    override fun forward(amount: Int) {
        horizontal += amount
    }
}

class Sub2: Submarine {
    var horizontal: Int = 0
        private set

    var depth: Int = 0
        private set

    var aim: Int = 0
        private set

    override fun down(amount: Int) {
        aim += amount
    }

    override fun up(amount: Int) {
        aim -= amount
    }

    override fun forward(amount: Int) {
        horizontal += amount
        depth += aim * amount
    }
}