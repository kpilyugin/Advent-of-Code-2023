fun main() {
    fun List<String>.horizontal(): List<Int> {
        fun reflects(i: Int): Int? {
            var i1 = i
            var i2 = i - 1
            while (i1 in indices && i2 in indices) {
                if (row(i1) != row(i2)) return null
                i1--
                i2++
            }
            return i
        }
        return (1 until size).mapNotNull { reflects(it) }
    }

    fun List<String>.vertical(): List<Int> {
        fun reflects(i: Int): Int? {
            var i1 = i
            var i2 = i - 1
            while (i1 in first().indices && i2 in first().indices) {
                if (column(i1) != column(i2)) return null
                i1--
                i2++
            }
            return i
        }
        return (1 until first().length).mapNotNull { reflects(it) }
    }

    fun List<String>.smudge(): Sequence<List<String>> = sequence {
        val matrix = map { it.toCharArray() }
        for (i in indices) {
            for (j in first().indices) {
                matrix[i][j] = if (matrix[i][j] == '.') '#' else '.'
                yield(matrix.map { it.joinToString(separator = "") })
                matrix[i][j] = if (matrix[i][j] == '.') '#' else '.'
            }
        }
    }

    fun part1(input: String): Int {
        return input.split("\n\n").sumOf { pattern ->
            val table = pattern.lines()
            table.vertical().sum() + table.horizontal().sum() * 100
        }
    }

    fun part2(input: String): Int {
        return input.split("\n\n").sumOf { pattern ->
            val table = pattern.lines()
            val originalVertical = table.vertical().toSet()
            val originalHorizontal = table.horizontal().toSet()
            table.smudge().map { version ->
                val vertical = version.vertical().filter { it !in originalVertical }
                val horizontal = version.horizontal().filter { it !in originalHorizontal }
                vertical.sum() + horizontal.sum() * 100
            }.first { it != 0 }
        }
    }

    val testInput = readInputText("Day13_test")
    check(part1(testInput), 405)
    check(part2(testInput), 400)

    val input = readInputText("Day13")
    part1(input).println()
    part2(input).println()
}