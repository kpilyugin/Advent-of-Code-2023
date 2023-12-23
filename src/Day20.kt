class Solution {
    val modules = HashMap<String, Module>()
    var totalHigh = 0
    var totalLow = 0

    abstract inner class Module(val name: String) {
        val next = mutableListOf<String>()

        abstract fun send(): List<Module>
        abstract fun receive(high: Boolean, from: String)

        protected fun sendNext(high: Boolean): List<Module> {
            val nextModules = next.mapNotNull { modules[it] }
            nextModules.forEach { it.receive(high, name) }
            if (high) totalHigh += next.size else totalLow += next.size
            return nextModules
        }
    }

    inner class FlipFlop(name: String) : Module(name) {
        private var on = false
        private var flipped = false

        override fun receive(high: Boolean, from: String) {
            if (!high) {
                on = !on
                flipped = true
            }
        }

        override fun send(): List<Module> {
            return if (flipped) {
                flipped = false
                sendNext(on)
            } else {
                emptyList()
            }
        }
    }

    inner class Broadcast(name: String) : Module(name) {
        private var value = false

        override fun send(): List<Module> = sendNext(value)

        override fun receive(high: Boolean, from: String) {
            value = high
        }
    }

    inner class Conjunction(name: String) : Module(name) {
        val inValues = HashMap<String, Boolean>()

        override fun send(): List<Module> = sendNext(inValues.any { !it.value })

        override fun receive(high: Boolean, from: String) {
            inValues[from] = high
        }
    }

    fun build(input: List<String>) {
        input.forEach {
            val (from, to) = it.split(" -> ")
            val fromModule = when {
                from.startsWith('%') -> FlipFlop(from.drop(1))
                from.startsWith('&') -> Conjunction(from.drop(1))
                else -> Broadcast(from)
            }
            fromModule.next.addAll(to.split(", "))
            modules[fromModule.name] = fromModule
        }
        modules.values.forEach { module ->
            module.next.forEach { next ->
                val conj = modules[next] as? Conjunction
                conj?.let {
                    conj.inValues[module.name] = false
                }
            }
        }
    }

    fun part1(): Int {
        repeat(1000) {
            val first = modules["broadcaster"]!!
            totalLow++
            first.receive(false, "")
            var cur = listOf(first)
            while (cur.isNotEmpty()) {
                cur = cur.flatMap { it.send() }
            }
        }
        return totalHigh * totalLow
    }

    fun part2(): Long {
        val firstTrue = HashMap<String, Long>()
        repeat(10000) { step ->
            val first = modules["broadcaster"]!!
            first.receive(false, "")
            var cur = listOf(first)
            while (cur.isNotEmpty()) {
                cur = cur.flatMap { it.send() }
                val conj = modules["cs"] as Conjunction
                conj.inValues.forEach { (key, value) ->
                    if (value && key !in firstTrue) {
                        firstTrue[key] = (step + 1).toLong()
                    }
                }
            }
        }
        println(firstTrue)
        return firstTrue.values.reduce { v1, v2 -> v1 * v2 }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Solution().run {
            build(input)
            part1()
        }
    }

    fun part2(input: List<String>): Long {
        return Solution().run {
            build(input)
            part2()
        }
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput), 32000000)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}