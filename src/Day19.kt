import kotlin.math.max
import kotlin.math.min

fun main() {
    class Rule(val desc: String) {
        val sign: Char
        val index: Int
        val value: Int
        val next: String

        init {
            if (':' in desc) {
                val (condition, next) = desc.split(':')
                this.next = next
                val name = condition[0]
                index = "xmas".indexOf(name)
                sign = condition[1]
                value = condition.drop(2).toInt()
            } else {
                sign = '-'
                index = 0
                value = 0
                next = desc
            }
        }

        fun apply(values: List<Int>): String? {
            return when (sign) {
                '<' -> if (values[index] < value) next else null
                '>' -> if (values[index] > value) next else null
                else -> desc
            }
        }

        fun <T> List<T>.replace(i: Int, v: T): List<T> {
            return ArrayList(this).also { it[i] = v }
        }

        fun split(ranges: List<IntRange>): Pair<List<IntRange>?, List<IntRange>?> {
            when (sign) {
                '<' -> {
                    val cur = ranges[index]
                    if (value in cur) {
                        val fit = IntRange(cur.first, value - 1)
                        val skip = IntRange(value, cur.last)
                        return ranges.replace(index, fit) to ranges.replace(index, skip)
                    } else {
                        return null to ranges
                    }
                }
                '>' -> {
                    val cur = ranges[index]
                    if (value in cur) {
                        val fit = IntRange(value + 1, cur.last)
                        val skip = IntRange(cur.first, value)
                        return ranges.replace(index, fit) to ranges.replace(index, skip)
                    } else {
                        return null to ranges
                    }
                }
                else -> {
                    return ranges to null
                }
            }
        }
    }


    fun parseWorkflows(input: String): Map<String, List<Rule>> {
        return input.lines().associate { line ->
            val name = line.substringBefore('{')
            val rules = line.substringAfter('{').substringBefore('}').split(',').map { Rule(it) }
            name to rules
        }
    }

    fun part1(input: String): Int {
        val (flows, parts) = input.split("\n\n")
        val workflows = parseWorkflows(flows)

        return parts.lines().sumOf { line ->
            val values = line.drop(1).dropLast(1).split(',').map { it.substringAfter("=").toInt() }
            var cur = "in"
            while (cur !in setOf("A", "R")) {
                val rules = workflows[cur]!!
                cur = rules.firstNotNullOf { it.apply(values) }
            }
            if (cur == "A") values.sum() else 0
        }
    }

    fun part2(input: String): Long {
        val workflows = parseWorkflows(input.substringBefore("\n\n"))

        var total = 0L
        fun process(name: String, ranges: List<IntRange>) {
            if (name == "A") {
                total += ranges.fold(1L) { value, range ->
                    value * max(range.last - range.first + 1, 0)
                }
            } else if (name != "R") {
                var current: List<IntRange>? = ranges
                val rules = workflows[name]!!
                rules.forEach { rule ->
                    if (current != null) {
                        val (fit, skip) = rule.split(current!!)
                        fit?.let { process(rule.next, fit) }
                        current = skip
                    }
                }
            }
        }
        process("in", List(4) { 1..4000 })
        return total
    }

    val testInput = readInputText("Day19_test")
    check(part1(testInput), 19114)
    check(part2(testInput), 167409079868000L)

    val input = readInputText("Day19")
    part1(input).println()
    part2(input).println()
}