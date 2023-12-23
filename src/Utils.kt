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

data class Point(val x: Int, val y: Int) {
    operator fun plus(dir: Dir): Point {
        return Point(x + dir.dx, y + dir.dy)
    }
}

enum class Dir(val dx: Int, val dy: Int) {
    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1);

    fun vertical() = this == UP || this == DOWN

    fun horizontal() = this == LEFT || this == RIGHT

    fun rotateLeft() = entries[(ordinal + 3) % 4]

    fun rotateRight() = entries[(ordinal + 1) % 4]
}

fun List<String>.row(i: Int): String = this[i]

fun List<String>.column(i: Int): String = map { it[i] }.joinToString(separator = "")

fun <T> List<T>.allPairs(): List<Pair<T, T>> {
    return indices.flatMap { second -> (0 until second).map { this[it] to this[second] } }
}

fun rangeBetween(a: Int, b: Int): IntRange = min(a, b)..max(a, b)

operator fun List<String>.contains(p: Point): Boolean = p.y in indices && p.x in first().indices

fun List<String>.valueAt(p: Point) = this[p.y][p.x]

fun List<String>.findStart(): Point {
    for (i in indices) {
        for (j in this[0].indices) {
            if (this[i][j] == 'S') return Point(j, i)
        }
    }
    throw IllegalStateException()
}