fun main() {
    fun part1(input: List<String>): Int {
        val (times, distances) = input.map { line ->
            line.substringAfter(":").trim().split(Regex(" +")).map { it.toInt() }
        }
        val wins = times.zip(distances).map { (time, distance) ->
            (1..time).count { speed -> speed * (time - speed) > distance }
        }
        return wins.reduce(Int::times)
    }

    fun part2(input: List<String>): Int {
        val (time, distance) = input.map { line ->
            line.substringAfter(":").replace(" ", "").toLong()
        }
        return (1..time).count { speed -> speed * (time - speed) > distance }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
