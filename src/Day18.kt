import kotlin.math.abs

fun main() {
    fun solve(commands: List<Pair<Dir, Int>>): Long {
        val vertices = mutableListOf(Point(0, 0))
        var perimeter = 0L
        var area = 0L
        commands.forEach { (dir, steps) ->
            val prev = vertices.last()
            val cur = Point(prev.x + dir.dx * steps, prev.y + dir.dy * steps)
            vertices += cur
            perimeter += steps
            area += cur.y.toLong() * prev.x - cur.x.toLong() * prev.y
        }
        vertices.dropLast(1)
        return abs(area) / 2 + perimeter / 2 + 1
    }

    fun part1(input: List<String>): Int {
        val commands = input.map {
            val chunks = it.split(" ")
            val dir = when (chunks[0][0]) {
                'R' -> Dir.RIGHT
                'L' -> Dir.LEFT
                'U' -> Dir.UP
                'D' -> Dir.DOWN
                else -> throw IllegalStateException()
            }
            val steps = chunks[1].toInt()
            dir to steps
        }
        return solve(commands).toInt()
    }

    fun part2(input: List<String>): Long {
        val commands = input.map {
            val code = it.substringAfter("(").substringBefore(")").drop(1)
            val dir = when (code.last()) {
                '0' -> Dir.RIGHT
                '1' -> Dir.DOWN
                '2' -> Dir.LEFT
                '3' -> Dir.UP
                else -> throw IllegalStateException()
            }
            val steps = code.dropLast(1).toInt(16)
            dir to steps
        }
        return solve(commands)
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput), 62)
    check(part2(testInput), 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}