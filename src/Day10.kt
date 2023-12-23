import kotlin.math.abs

fun main() {
    val dirs = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    fun markLoopDistances(input: List<String>): Array<IntArray> {
        val n = input.size
        val m = input[0].length

        val dist = Array(n) { IntArray(m) { -1 } }
        val start = input.findStart()
        dist[start.y][start.x] = 0

        val turns = mapOf(
            '-' to (Dir.LEFT to Dir.RIGHT),
            '|' to (Dir.UP to Dir.DOWN),
            'F' to (Dir.DOWN to Dir.RIGHT),
            'L' to (Dir.UP to Dir.RIGHT),
            'J' to (Dir.UP to Dir.LEFT),
            '7' to (Dir.LEFT to Dir.DOWN)
        )

        fun Dir.matches(from: Point, to: Point) = from.x - to.x == dx && from.y - to.y == dy

        fun canGo(cur: Point, next: Point): Boolean {
            val curTurn = input[cur.y][cur.x]
            if (curTurn != 'S') {
                val (from, to) = turns[curTurn] ?: return false
                if (!from.matches(next, cur) && !to.matches(next, cur)) return false
            }
            val (from, to) = turns[input[next.y][next.x]] ?: return false
            return from.matches(cur, next) || to.matches(cur, next)
        }

        val visited = HashSet<Point>()
        val queue = ArrayDeque<Point>()
        queue.addLast(start)
        visited += start
        dist[start.y][start.x] = 0
        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            for ((i, j) in dirs) {
                val next = Point(cur.x + i, cur.y + j)
                if (next in visited) continue
                if (next.y !in 0 until n || next.x !in 0 until m) continue
                if (canGo(cur, next)) {
                    dist[next.y][next.x] = dist[cur.y][cur.x] + 1
                    visited += next
                    queue.addLast(next)
                }
            }
        }
        return dist
    }

    fun part1(input: List<String>): Int {
        val dist = markLoopDistances(input)
        return dist.maxOf { it.max() }
    }

    fun part2(input: List<String>): Int {
        val dist = markLoopDistances(input)
        val n = dist.size
        val m = dist[0].size
        val N = 2 * n + 3
        val M = 2 * m + 3
        val large = Array(N) { IntArray(M) { -1 } }
        for (i in 0 until n) {
            for (j in 0 until m) {
                large[2 * i + 1][2 * j + 1] = dist[i][j]
            }
        }
        for (i in 1 until N - 1) {
            for (j in 1 until M - 1) {
                fun isAdjacent(a: Int, b: Int) = a >= 0 && b >= 0 && abs(a - b) == 1
                if (large[i][j] == -1 &&
                    (isAdjacent(large[i][j - 1], large[i][j + 1]) || isAdjacent(large[i - 1][j], large[i + 1][j]))
                ) {
                    large[i][j] = 10
                }
            }
        }
        val reachable = Array(N) { BooleanArray(M) }
        val queue = ArrayDeque<Point>()
        queue.addLast(Point(0, 0))
        while (queue.isNotEmpty()) {
            val (i, j) = queue.removeFirst()
            if (i !in large.indices || j !in large[0].indices) continue
            if (!reachable[i][j] && large[i][j] == -1) {
                reachable[i][j] = true
                dirs.forEach {
                    val next = Point(i + it.first, j + it.second)
                    queue.addLast(next)
                }
            }
        }
        var total = 0
        for (i in 0 until N) {
            for (j in 0 until M) {
                if (large[i][j] == -1 && i % 2 == 1 && j % 2 == 1 && !reachable[i][j]) {
                    total++
                }
            }
        }
        return total
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}