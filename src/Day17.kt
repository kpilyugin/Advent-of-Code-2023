import java.util.PriorityQueue

fun main() {
    data class Position(val point: Point, val dir: Dir, val sameDirCount: Int)

    data class State(val pos: Position, val cost: Int)

    fun solve(input: List<String>, minConsecutive: Int, maxConsecutive: Int): Int {
        val n = input.size
        val m = input[0].length

        val visited = HashSet<Position>()
        val queue = PriorityQueue<State>(compareBy { it.cost })

        val initial = State(Position(Point(0, 0), Dir.RIGHT, 0), 0)
        queue += initial
        visited += initial.pos

        fun tryAdd(cur: State, dir: Dir) {
            val continues = dir == cur.pos.dir && cur.pos.sameDirCount > 0
            val sameDirCount = if (continues) cur.pos.sameDirCount + 1 else minConsecutive
            val curPoint = cur.pos.point
            val steps = if (continues) 1 else minConsecutive
            val new = Point(curPoint.x + steps * dir.dx, curPoint.y + steps * dir.dy)
            if (sameDirCount in minConsecutive..maxConsecutive
                && new.x in 0 until m && new.y in 0 until n
            ) {
                val newPos = Position(new, dir, sameDirCount)
                if (newPos !in visited) {
                    val cost = cur.cost + (1..steps).sumOf {
                        input[curPoint.y + it * dir.dy][curPoint.x + it * dir.dx].digitToInt()
                    }
                    visited += newPos
                    queue += State(newPos, cost)
                }
            }
        }

        while (queue.isNotEmpty()) {
            val cur = queue.remove()
            if (cur.pos.point == Point(m - 1, n - 1)) {
                return cur.cost
            }
            val dir = cur.pos.dir
            tryAdd(cur, dir)
            tryAdd(cur, dir.rotateLeft())
            tryAdd(cur, dir.rotateRight())
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        return solve(input, 1, 3)
    }

    fun part2(input: List<String>): Int {
        return solve(input, 4, 10)
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput), 102)
    check(part2(testInput), 94)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}