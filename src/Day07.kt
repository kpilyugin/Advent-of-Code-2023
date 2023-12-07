fun main() {
    class Card(line: String) {
        val card = line.substringBefore(" ")
        val bid = line.substringAfter(" ").toInt()
        val joker = 'J'

        fun groups(withJoker: Boolean): List<Int> {
            return if (withJoker) {
                val freqs = card.groupBy { it }.mapValues { it.value.size }.toMutableMap()
                val best = freqs.filter { it.key != joker }.maxByOrNull { it.value }
                if (joker in card && best != null) {
                    freqs[best.key] = best.value + freqs[joker]!!
                    freqs.remove(joker)
                }
                freqs.values.sortedDescending()
            } else {
                card.groupBy { it }
                    .map { it.value.size }.sortedDescending()
            }
        }

        override fun toString(): String {
            return card
        }
    }


    fun compareSameType(c1: String, c2: String, withJoker: Boolean): Int {
        val order = if (withJoker) "J23456789TQKA" else "23456789TJQKA"
        for (i in c1.indices) {
            if (c1[i] != c2[i]) {
                return order.indexOf(c1[i]) - order.indexOf(c2[i])
            }
        }
        return 0
    }

    fun compareGroups(g1: List<Int>, g2: List<Int>): Int {
        if (g1.size != g2.size) {
            return g2.size - g1.size
        }
        for (i in g1.indices) {
            if (g1[i] != g2[i]) {
                return g1[i] - g2[i]
            }
        }
        return 0
    }

    fun compareRanks(c1: Card, c2: Card, withJoker: Boolean): Int {
        val res = compareGroups(c1.groups(withJoker), c2.groups(withJoker))
        return if (res != 0) res else compareSameType(c1.card, c2.card, withJoker)
    }

    fun solve(input: List<String>, withJoker: Boolean): Int {
        return input.map { Card(it) }
            .sortedWith { c1, c2 -> compareRanks(c1, c2, withJoker) }
            .withIndex()
            .sumOf { it.value.bid * (it.index + 1) }
    }

    fun part1(input: List<String>) = solve(input, withJoker = false)

    fun part2(input: List<String>) = solve(input, withJoker = true)

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}