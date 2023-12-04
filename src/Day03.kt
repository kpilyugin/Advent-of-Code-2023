fun main() {

    fun part1(input: List<String>): Int {
        fun containsSymbol(x: Int, y1: Int, y2: Int): Boolean {
            for (i in x - 1..x + 1) {
                for (j in y1 - 1..y2 + 1) {
                    if (i in input.indices && j in input[0].indices) {
                        val cur = input[i][j]
                        if (!cur.isDigit() && cur != '.') return true
                    }
                }
            }
            return false
        }

        var sum = 0
        var current = 0
        var from = -1
        input.forEachIndexed { i, line ->
            for (j in 0..line.length) {
                if (j < line.length && line[j].isDigit()) {
                    current = 10 * current + line[j].digitToInt()
                    if (from == -1) from = j
                } else {
                    if (from != -1 && containsSymbol(i, from, j - 1)) {
                        sum += current
                    }
                    current = 0
                    from = -1
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        fun String.number(i: Int): Int {
            var from = i
            while (from in indices && this[from].isDigit()) from--
            var to = i
            while (to in indices && this[to].isDigit()) to++
            return substring(from + 1..to - 1).toInt()
        }
        var sum = 0
        fun addStar(x: Int, y: Int) {
            val digits = hashSetOf<Int>()
            for (i in x - 1 .. x + 1) {
                for (j in y - 1 .. y + 1) {
                    if (i in input.indices && j in input[0].indices && input[i][j].isDigit()) {
                        digits += input[i].number(j)
                    }
                }
            }
            if (digits.size == 2) {
                sum += digits.reduce { a, b -> a * b }
            }
        }
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, ch ->
                if (ch == '*') addStar(i, j)
            }
        }
        return sum
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
