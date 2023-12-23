fun main() {
    fun intersects(from1: Int, to1: Int, from2: Int, to2: Int): Boolean {
        return to1 >= from2 && to2 >= from1
    }

    class Brick(val from: IntArray, val to: IntArray) {
        val supports = ArrayList<Brick>()
        val supportedBy = ArrayList<Brick>()

        fun minZ() = from[2]

        fun move() {
            from[2]--
            to[2]--
        }

        fun supports(other: Brick): Boolean {
            val intersectXY = (0..1).all { intersects(from[it], to[it], other.from[it], other.to[it]) }
            val supportZ = intersects(from[2], to[2], other.from[2] - 1, other.to[2] - 1)
            return intersectXY && supportZ
        }

        override fun toString(): String {
            return "${from.contentToString()} - ${to.contentToString()}"
        }
    }

    fun collectBricks(input: List<String>): List<Brick> {
        val bricks = input.map { line ->
            val (from, to) = line.split("~")
                .map {
                        part -> part.split(",").map { it.toInt() }.toIntArray()
                }
            Brick(from, to)
        }
        val static = HashSet<Brick>()
        var moving = ArrayList(bricks)
        while (moving.isNotEmpty()) {
            val toMove = moving.sortedBy { it.minZ() }
            val stillMoving = ArrayList<Brick>()
            for (cur in toMove) {
                if (cur.minZ() == 1 || static.any { it.supports(cur) }) {
                    static += cur
                } else {
                    cur.move()
                    stillMoving += cur
                }
            }
            moving = stillMoving
        }

        for (brick in static) {
            brick.supports += static.filter { it != brick && brick.supports(it) }
            brick.supportedBy += static.filter { it != brick && it.supports(brick) }
        }
        return bricks.toList()
    }

    fun part1(input: List<String>): Int {
        val bricks = collectBricks(input)
        return bricks.count {
            it.supports.isEmpty() || it.supports.all { it.supportedBy.size > 1 }
        }
    }

    fun part2(input: List<String>): Int {
        fun fallFrom(cur: Brick, fallen: MutableSet<Brick>): Int {
            val next = cur.supports.filter { candidate ->
                candidate !in fallen && candidate.supportedBy.all { it in fallen }
            }
            fallen += next
            return next.size + next.sumOf { fallFrom(it, fallen) }
        }
        val bricks = collectBricks(input)
        return bricks.sumOf { fallFrom(it, mutableSetOf(it)) }
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput), 5)
    check(part2(testInput), 7)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}