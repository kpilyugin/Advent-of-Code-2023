import kotlin.math.max
import kotlin.math.min

fun main() {
    fun LongRange.intersect(other: LongRange): LongRange? {
        val start = max(first, other.first)
        val end = min(last, other.last)
        return if (end > start) LongRange(start, end) else null
    }

    fun LongRange.subtract(subRanges: List<LongRange>): List<LongRange> {
        val result = mutableListOf<LongRange>()
        var prev = start - 1

        for (sub in subRanges.sortedBy { it.first }) {
            if (sub.first > prev + 1) {
                result += prev + 1 until sub.first
            }
            prev = sub.last
        }
        if (last > prev) result += prev + 1..last
        return result
    }

    data class RangeMapping(val destStart: Long, val sourceStart: Long, val length: Long) {
        private val sourceRange = LongRange(sourceStart, sourceStart + length)

        private fun remap(source: Long) = destStart + source - sourceStart

        fun apply(source: Long): Long? {
            return if (source in sourceRange) remap(source) else null
        }

        fun apply(range: LongRange): Pair<LongRange, LongRange>? {
            return sourceRange.intersect(range)?.let { source ->
                source to LongRange(remap(source.first), remap(source.last))
            }
        }
    }

    class Mapping(val ranges: List<RangeMapping>) {
        fun apply(source: Long): Long {
            return ranges.firstNotNullOfOrNull { it.apply(source) } ?: source
        }

        fun apply(source: LongRange): List<LongRange> {
            val mapped = mutableListOf<LongRange>()
            val targets = mutableListOf<LongRange>()
            ranges.forEach {
                it.apply(source)?.let { (from, to) ->
                    mapped += from
                    targets += to
                }
            }
            targets += source.subtract(mapped)
            return targets
        }
    }

    fun parseMappings(parts: List<String>) = parts.drop(1).map { part ->
        val lines = part.split("\n")
        val ranges = lines.drop(1).map { line ->
            val nums = line.split(" ").map { it.toLong() }
            RangeMapping(nums[0], nums[1], nums[2])
        }
        Mapping(ranges)
    }

    fun part1(input: String): Long {
        val parts = input.split("\n\n")
        val seeds = parts[0].substringAfter(": ").split(' ').map { it.toLong() }
        val mappings = parseMappings(parts)
        return seeds.minOf { seed ->
            mappings.fold(seed) { value, mapping -> mapping.apply(value) }
        }
    }

    fun part2(input: String): Long {
        val parts = input.split("\n\n")
        val seeds = parts[0].substringAfter(": ").split(' ')
            .map { it.toLong() }.chunked(2).map { LongRange(it[0], it[0] + it[1]) }
        val mappings = parseMappings(parts)
        return seeds.minOf { seed ->
            val finalRanges = mappings.fold(listOf(seed)) { curRanges, mapping ->
                curRanges.flatMap { mapping.apply(it) }
            }
            finalRanges.minOf { it.first }
        }
    }

    val testInput = readInputText("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInputText("Day05")
    part1(input).println()
    part2(input).println()
}
