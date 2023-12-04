fun main() {
    class Card(val winning: List<Int>, val own: List<Int>) {
        var copies = 1

        fun copy(times: Int) {
            copies += times
        }

        fun winningCount() = own.count { it in winning }
    }

    fun parseCards(input: List<String>) = input.map { line ->
        line.substringAfter(": ")
            .split(" | ")
            .map { list -> list.trim().split(Regex(" +")).map { it.toInt() } }
            .let { Card(it[0], it[1]) }
    }

    fun part1(input: List<String>): Int {
        return parseCards(input).sumOf { card ->
            val count = card.winningCount()
            if (count > 0) 1 shl (count - 1) else 0
        }
    }

    fun part2(input: List<String>): Int {
        val cards = parseCards(input)
        cards.forEachIndexed { index, card ->
            cards.drop(index + 1).take(card.winningCount()).forEach { it.copy(card.copies) }
        }
        return cards.sumOf { it.copies }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
