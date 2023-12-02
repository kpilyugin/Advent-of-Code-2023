import kotlin.math.max

fun main() {
    fun String.isGamePossible(): Boolean {
        val sets = split("; ")
        return sets.all { set ->
            val cubes = set.split(", ")
            val cubesMap = hashMapOf<String, Int>()
            cubes.forEach {
                val (count, type) = it.split(" ")
                cubesMap[type] = count.toInt()
            }
            fun String.count() = cubesMap[this] ?: 0
            "red".count() <= 12 && "green".count() <= 13 && "blue".count() <= 14
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (name, desc) = line.split(": ")
            if (desc.isGamePossible()) {
                name.split(" ")[1].toInt()
            } else 0
        }
    }

    fun String.power(): Int {
        val sets = split("; ")
        val maxCounts = hashMapOf<String, Int>()
        sets.forEach { set ->
            val cubes = set.split(", ")
            cubes.forEach {
                val (count, type) = it.split(" ")
                maxCounts[type] = max(maxCounts[type] ?: 0, count.toInt())
            }
        }
        fun String.maxCount() = maxCounts[this] ?: 0
        return "red".maxCount() * "green".maxCount() * "blue".maxCount()
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val game = line.split(": ")[1]
            game.power()
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
