
data class Vertex(val point: Point) {
    val edges = mutableListOf<Edge>()

    fun replace(v: Vertex, other: Vertex, steps: Int) {
        edges.removeIf { it.next == v }
        edges += Edge(other, steps)
    }
}

data class Edge(val next: Vertex, val steps: Int)

fun main() {
    fun part1(grid: List<String>): Int {
        fun findLongest(p: Point, steps: Int, visited: MutableSet<Point>): Int {
            if (p.y == grid.lastIndex) {
                return steps
            }
            val dirs = when (grid.valueAt(p)) {
                '^' -> listOf(Dir.UP)
                '>' -> listOf(Dir.RIGHT)
                'v' -> listOf(Dir.DOWN)
                '<' -> listOf(Dir.LEFT)
                '.' -> Dir.entries
                else -> throw IllegalStateException()
            }
            visited += p
            val next = dirs.map { p + it }
                .filter { it in grid && grid.valueAt(it) != '#' && it !in visited }
            val longest = next.maxOfOrNull { findLongest(it, steps + 1, visited) } ?: 0
            visited -= p
            return longest
        }
        val startX = grid[0].indexOf('.')
        return findLongest(Point(startX, 0), 0, HashSet())
    }

    fun part2(grid: List<String>): Int {
        val vertices = HashMap<Point, Vertex>()
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                val p = Point(x, y)
                if (grid.valueAt(p) != '#') {
                    vertices[p] = Vertex(p)
                }
            }
        }
        vertices.forEach { (p, v) ->
            v.edges += Dir.entries.map { p + it }
                .mapNotNull { vertices[it] }
                .map { Edge(it, 1) }
        }
        val removed = mutableSetOf<Vertex>()
        for (v in vertices.values) {
            if (v.edges.size == 2) {
                val (prev, prevSteps) = v.edges[0]
                val (next, nextSteps) = v.edges[1]
                prev.replace(v, next, prevSteps + nextSteps)
                next.replace(v, prev, nextSteps + prevSteps)
                removed += v
            }
        }
        vertices.values.removeAll(removed)

        fun findLongest(v: Vertex, steps: Int, visited: MutableSet<Vertex>): Int {
            if (v.point.y == grid.lastIndex) {
                return steps
            }
            visited += v
            val longest = v.edges.filter { it.next !in visited }
                .maxOfOrNull { findLongest(it.next, steps + it.steps, visited) } ?: 0
            visited -= v
            return longest
        }
        val startX = grid[0].indexOf('.')
        val first = vertices[Point(startX, 0)]!!
        return findLongest(first, 0, HashSet())
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput), 94)
    check(part2(testInput), 154)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}