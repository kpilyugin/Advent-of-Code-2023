fun main() {

    fun solve(input: List<String>, emptySize: Int): Long {
        val galaxies = input.flatMapIndexed { i, row ->
            row.withIndex().filter { it.value == '#' }.map { Point(i, it.index) }
        }
        val emptyRows = input.indices.filter { i -> input.row(i).all { it == '.' } }.toSet()
        val emptyColumns = input[0].indices.filter { j -> input.column(j).all { it == '.' } }.toSet()
        return galaxies.allPairs().sumOf {
            rangeBetween(it.first.x, it.second.x).sumOf { row ->
                if (row in emptyRows) emptySize.toLong() else 1
            } + rangeBetween(it.first.y, it.second.y).sumOf { column ->
                if (column in emptyColumns) emptySize.toLong() else 1
            } - 2
        }
    }

    fun part1(input: List<String>) = solve(input, 2)

    fun part2(input: List<String>) = solve(input, 1000000)

    val testInput = readInput("Day11_test")
    check(part1(testInput), 374L)
    check(solve(testInput, 10), 1030L)
    check(solve(testInput, 100), 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}