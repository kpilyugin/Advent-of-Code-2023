fun main() {
    fun getVisited(grid: List<String>): Map<Point, Int> {
        val start = grid.findStart()

        val visited = mutableMapOf<Point, Int>()
        visited[start] = 0

        var steps = 1
        var cur = setOf(start)
        while (cur.isNotEmpty()) {
            val next = cur.flatMap { prev ->
                Dir.entries
                    .map { dir -> prev + dir }
                    .filter { it in grid && it !in visited && grid.valueAt(it) == '.' }
            }.toSet()
            next.forEach {
                visited[it] = steps
            }
            cur = next
            steps++
        }
        return visited
    }

    fun part1(grid: List<String>, steps: Int): Int {
        val visited = getVisited(grid)
        return visited.count { it.value <= steps && it.value % 2 == steps % 2 }
    }

    fun part2(grid: List<String>, totalSteps: Int): Long {
        val size = grid.size
        val visited = getVisited(grid)

        val n = ((totalSteps - (size / 2)) / size).toLong()
        val evenCorners = visited.count { it.value % 2 == 0 && it.value > 65 }.toLong()
        val oddCorners = visited.count { it.value % 2 == 1 && it.value > 65 }.toLong()
        val evenFull = visited.count { it.value % 2 == 0 }.toLong()
        val oddFull = visited.count { it.value % 2 == 1 }.toLong()
        return (n + 1) * (n + 1) * oddFull + n * n * evenFull - (n + 1) * oddCorners + n * evenCorners - n
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput, 6), 16)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input, 26501365).println()
}