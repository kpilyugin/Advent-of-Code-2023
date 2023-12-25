import io.ksmt.KContext
import io.ksmt.expr.KBitVec64Value
import io.ksmt.expr.KExpr
import io.ksmt.solver.z3.KZ3Solver
import io.ksmt.sort.KBvSort
import io.ksmt.utils.getValue
import io.ksmt.utils.mkConst
import kotlin.time.Duration.Companion.seconds

fun main() {
    data class Vec3(val x: Long, val y: Long, val z: Long)

    class Stone(val pos: Vec3, val velocity: Vec3) {
        fun intersectsXY(other: Stone, from: Long, to: Long): Boolean {
            // x1 + vx1 * t1 = x2 + vx2 * t2
            // y1 + vy1 * t1 = y2 + vy2 * t2
            // t2 = ((x1 - x2) + vx1 * t1) / vx2
            // (y1 - y2 + vy1 * t1) * vx2 = vy2 * (x1 - x2 + vx1 * t1)
            // t1 * (vx2 * vy1 - vx1 * vy2) = vy2 * (x1 - x2) + vx2 * (y2 - y1)
            val x1 = pos.x
            val y1 = pos.y
            val x2 = other.pos.x
            val y2 = other.pos.y
            val vx1 = velocity.x
            val vy1 = velocity.y
            val vx2 = other.velocity.x
            val vy2 = other.velocity.y
            if (vx1 * vy2 == vy1 * vx2) {
                return false
            }
            val t1 = (vy2 * (x1 - x2) + vx2 * (y2 - y1)).toDouble() / (vx2 * vy1 - vx1 * vy2)
            val t2 = ((x1 - x2) + vx1 * t1) / vx2
            if (t1 < 0 || t2 < 0) {
                return false
            }
            fun inside(v: Double) = v >= from && v <= to
            return inside(x1 + vx1 * t1) && inside(y1 + vy1 * t1)
        }
    }

    fun parseStones(input: List<String>) = input.map { line ->
        val (pos, velocity) = line.split(" @ ").map { part ->
            part.split(",").map { it.trim().toLong() }.let { Vec3(it[0], it[1], it[2]) }
        }
        Stone(pos, velocity)
    }

    fun part1(input: List<String>, from: Long, to: Long): Int {
        val stones = parseStones(input)
        return stones.allPairs().count {
            it.first.intersectsXY(it.second, from, to)
        }
    }

    fun part2(input: List<String>): Long {
        val stones = parseStones(input)
        val context = KContext()
        return with(context) {
            operator fun <T : KBvSort> KExpr<T>.plus(other: KExpr<T>): KExpr<T> = mkBvAddExpr(this, other)
            operator fun <T : KBvSort> KExpr<T>.plus(other: Long): KExpr<T> = plus(mkBv(other, sort))
            operator fun <T : KBvSort> KExpr<T>.times(other: KExpr<T>): KExpr<T> = mkBvMulExpr(this, other)
            operator fun <T : KBvSort> KExpr<T>.times(other: Long): KExpr<T> = times(mkBv(other, sort))

            val x by bv64Sort
            val y by bv64Sort
            val z by bv64Sort
            val vx by bv64Sort
            val vy by bv64Sort
            val vz by bv64Sort

            val constraints = stones.take(3).flatMapIndexed { index, stone ->
                val t = bv64Sort.mkConst("t$index")
                listOf(
                    context.mkBvSignedGreaterOrEqualExpr(t, mkBv(0L)),
                    (x + vx * t) eq ((t * stone.velocity.x) + stone.pos.x),
                    (y + vy * t) eq ((t * stone.velocity.y) + stone.pos.y),
                    (z + vz * t) eq ((t * stone.velocity.z) + stone.pos.z)
                )
            }
            with(KZ3Solver(context)) {
                assert(mkAnd(constraints))
                check(timeout = 10.seconds)
                val resultExpr = model().eval(x + y + z)
                println(resultExpr)
                val result = (resultExpr as KBitVec64Value).longValue and 0x0FFFFFFFFFFFFFFFL
                result
            }
        }
    }

    val testInput = readInput("Day24_test")
    check(part1(testInput, 7, 27), 2)
    check(part2(testInput), 47L)

    val input = readInput("Day24")
    part1(input, 200000000000000L, 400000000000000L).println()
    part2(input).println()
}


