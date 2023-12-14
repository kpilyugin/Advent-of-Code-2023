import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.min

fun readInput(name: String) = Path("src/$name.txt").readLines()

fun readInputText(name: String) = Path("src/$name.txt").readText()

fun Any?.println() = println(this)

fun check(actual: Any, expected: Any) {
    if (actual != expected) {
        throw IllegalStateException("Expected $expected, but got $actual")
    }
}

data class Point(val x: Int, val y: Int)

enum class Dir(val dx: Int, val dy: Int) {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1)
}

fun List<String>.row(i: Int): String = this[i]

fun List<String>.column(i: Int): String = map { it[i] }.joinToString(separator = "")

fun <T> List<T>.allPairs(): List<Pair<T, T>> {
    return indices.flatMap { second -> (0 until second).map { this[it] to this[second] } }
}

fun rangeBetween(a: Int, b: Int): IntRange = min(a, b)..max(a, b)