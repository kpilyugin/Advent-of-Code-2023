fun main() {
    data class State(val pos: Point, val dir: Dir)

    fun State.next(actualDir: Dir = dir): State {
        return State(Point(pos.x + actualDir.dx, pos.y + actualDir.dy), actualDir)
    }

    fun Dir.reflectLeft(): Dir = when (this) {
        Dir.LEFT -> Dir.UP
        Dir.UP -> Dir.LEFT
        Dir.RIGHT -> Dir.DOWN
        Dir.DOWN -> Dir.RIGHT
    }

    fun Dir.reflectRight(): Dir = when (this) {
        Dir.LEFT -> Dir.DOWN
        Dir.DOWN -> Dir.LEFT
        Dir.RIGHT -> Dir.UP
        Dir.UP -> Dir.RIGHT
    }

    fun solve(input: List<String>, initialState: State): Int {
        val visited = mutableSetOf<State>()

        var states = listOf(initialState)
        visited.addAll(states)
        while (states.isNotEmpty()) {
            val newStates: List<State> = states.flatMap {
                val pos = it.pos
                when (input[pos.y][pos.x]) {
                    '.' -> listOf(it.next())
                    '|' -> if (it.dir.vertical()) listOf(it.next()) else listOf(it.next(Dir.UP), it.next(Dir.DOWN))
                    '-' -> if (it.dir.horizontal()) listOf(it.next()) else listOf(it.next(Dir.LEFT), it.next(Dir.RIGHT))
                    '\\' -> listOf(it.next(it.dir.reflectLeft()))
                    '/' -> listOf(it.next(it.dir.reflectRight()))
                    else -> throw IllegalStateException()
                }
            }.filter { it !in visited && it.pos.x in input[0].indices && it.pos.y in input.indices }
            visited.addAll(newStates)
            states = newStates
        }
        return visited.map { it.pos }.toSet().size
    }

    fun part1(input: List<String>): Int {
        return solve(input, State(Point(0, 0), Dir.RIGHT))
    }

    fun part2(input: List<String>): Int {
        val states = input.indices.map { State(Point(0, it), Dir.RIGHT) } +
                input.indices.map { State(Point(input[0].lastIndex, it), Dir.LEFT) } +
                input[0].indices.map { State(Point(it, 0), Dir.DOWN) } +
                input[0].indices.map { State(Point(it, input.lastIndex), Dir.UP) }
        return states.maxOf { initial ->
            solve(input, initial)
        }
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput), 46)
    check(part2(testInput), 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}