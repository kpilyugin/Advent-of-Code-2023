fun main() {
    fun parseMoves(input: List<String>) = input.drop(2).associate {
        val from = it.substringBefore(" ")
        val left = it.substringAfter("(").substringBefore(",")
        val right = it.substringAfter(", ").dropLast(1)
        from to (left to right)
    }

    fun part1(input: List<String>): Int {
        val pattern = input[0]
        val moves = parseMoves(input)
        var cur = "AAA"
        var pos = 0
        var steps = 0
        while (cur != "ZZZ") {
            val direction = pattern[pos]
            cur = if (direction == 'L') moves[cur]!!.first else moves[cur]!!.second
            pos = (pos + 1) % pattern.length
            steps++
        }
        return steps
    }

    fun toPrimes(n: Int): List<Int> {
        var cur = n
        val res = mutableListOf<Int>()
        for (p in 2 until n) {
            if (cur % p == 0) {
                cur /= p
                res += p
            }
        }
        if (cur != 1) res += cur
        return res
    }

    fun part2(input: List<String>): Long {
        val pattern = input[0]
        val moves = parseMoves(input)

        fun findCycle(start: String): Int {
            var cur = start
            var pos = 0
            var steps = 0
            while (!cur.endsWith('Z')) {
                steps++
                val isLeft = pattern[pos] == 'L'
                cur = if (isLeft) moves[cur]!!.first else moves[cur]!!.second
                pos = (pos + 1) % pattern.length
            }
            return steps
        }
        return moves.keys.asSequence()
            .filter { it.endsWith("A") }
            .map { findCycle(it) }
            .flatMap { toPrimes(it) }
            .toSet()
            .map { it.toLong() }
            .reduce { p1, p2 -> p1 * p2 }
    }

    val testInput1 = readInput("Day08_test")
    check(part1(testInput1) == 6)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}