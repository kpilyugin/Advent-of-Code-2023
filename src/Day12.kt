fun main() {

    fun countArrangements(pattern: CharArray, target: List<Int>): Long {
        data class State(val pos: Int, val target: List<Int>, val inside: Boolean)

        val memo = HashMap<State, Long>()

        fun solve(i: Int, target: List<Int>, inside: Boolean): Long {
            val state = State(i, target, inside)
            memo[state]?.let { return it }

            if (i == pattern.size) {
                return if (target == listOf(0)) 1 else 0
            }
            var result = 0L
            if (pattern[i] != '.' && target.first() != 0) {
                val next = target.toMutableList().also { it[0]-- }
                result += solve(i + 1, next, next.first() != 0)
            }
            if (pattern[i] != '#' && !inside) {
                val next = target.toMutableList().also { if (it.size > 1 && it.first() == 0) it.removeFirst() }
                result += solve(i + 1, next, false)
            }
            memo[state] = result
            return result
        }
        return solve(0, target, false)
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val pattern = line.substringBefore(" ").toCharArray()
            val target = line.substringAfter(" ").split(",").map { it.toInt() }
            countArrangements(pattern, target)
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val pattern = line.substringBefore(" ")
            val fullPattern = (1..5).joinToString(separator = "?") { pattern }.toCharArray()

            val target = line.substringAfter(" ")
            val fullTarget = (1..5).joinToString(separator = ",") { target }.split(",").map { it.toInt() }
            countArrangements(fullPattern, fullTarget)
        }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput), 21L)
    check(part2(testInput), 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}