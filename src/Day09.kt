fun main() {
    class Result(val previous: Int, val next: Int)

    fun solve(line: String): Result {
        val all = mutableListOf<MutableList<Int>>()
        all += line.split(" ").map { it.toInt() }.toMutableList()
        while (all.last().any { it != 0 }) {
            val next = mutableListOf<Int>()
            val cur = all.last()
            for (i in 1..cur.lastIndex) {
                next += cur[i] - cur[i - 1]
            }
            all += next
        }

        var next = 0
        var previous = 0
        for (i in all.lastIndex downTo 0) {
            next += all[i].last()
            previous = all[i].first() - previous
        }
        return Result(previous, next)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { solve(it).next }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { solve(it).previous }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}