fun main() {

    fun <T> Iterable<T>.split(shouldSplit: (T, T) -> Boolean): List<List<T>> {
        val result = mutableListOf<List<T>>()
        var chunk = mutableListOf<T>()
        for (cur in this) {
            if (chunk.isNotEmpty() && shouldSplit(chunk.last(), cur)) {
                result.add(chunk)
                chunk = mutableListOf(cur)
            } else {
                chunk.add(cur)
            }
        }
        if (chunk.isNotEmpty()) result.add(chunk)
        return result
    }

    fun part1(input: List<String>): Int {
        return (0 until input[0].length).sumOf { col ->
            val column = input.column(col)
            column.withIndex()
                .filter { it.value != '#' }
                .split { prev, next -> next.index > prev.index + 1 }
                .sumOf { chunk ->
                    val rocks = chunk.count { it.value == 'O' }
                    val first = input.size - chunk.first().index - rocks + 1
                    val last = input.size - chunk.first().index
                    (first..last).sum()
                }
        }
    }

    fun part2(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }
        val n = matrix.size
        val m = matrix[0].size

        fun swap(i1: Int, j1: Int, i2: Int, j2: Int) {
            val t = matrix[i1][j1]
            matrix[i1][j1] = matrix[i2][j2]
            matrix[i2][j2] = t
        }
        fun nextInDir(i: Int, j: Int, di: Int, dj: Int): Pair<Int, Int>? {
            var ci = i + di
            var cj = j + dj
            var res: Pair<Int, Int>? = null
            while (ci in matrix.indices && cj in matrix[0].indices) {
                if (matrix[ci][cj] == '.') {
                    res = ci to cj
                }
                if (matrix[ci][cj] == '#') break
                ci += di
                cj += dj
            }
            return res
        }
        fun tilt(di: Int, dj: Int) {
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (matrix[i][j] == 'O') {
                        nextInDir(i, j, di, dj)?.let {
                            swap(i, j, it.first, it.second)
                        }
                    }
                }
            }
        }

        val memo = HashMap<List<String>, Int>()
        fun findCycle(): Pair<Int, Int> {
            var step = 1
            while (true) {
                tilt(-1, 0)
                tilt(0, -1)
                tilt(1, 0)
                tilt(0, 1)
                val cur = matrix.map { it.joinToString(separator = "") }
                if (cur in memo) {
                    val first = memo[cur]!!
                    return first to step - first
                } else {
                    memo[cur] = step
                }
                step++
            }
        }

        val (first, period) = findCycle()
        val goal = 1000000000
        val target = (goal - first) % period + first
        println("$first, $period, $target")
        val state = memo.entries.first { it.value == target }.key

        fun List<String>.load(): Int {
            return withIndex().sumOf { row ->
                (n - row.index) * row.value.count { it == 'O' }
            }
        }
        return state.load()
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput), 136)
    check(part2(testInput), 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}