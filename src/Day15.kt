fun main() {
    fun String.hash(): Int = fold(0) { prev, char ->
        (prev + char.code) * 17 % 256
    }

    fun part1(input: String): Int {
        return input.split(",").sumOf { it.hash() }
    }

    data class Slot(val title: String, var value: Int)

    class Box {
        val slots = mutableListOf<Slot>()

        fun power() = slots.withIndex().sumOf { (it.index + 1) * it.value.value }
    }

    fun part2(input: String): Int {
        val boxes = Array(256) { Box() }
        input.split(",").forEach { cmd ->
            if ('=' in cmd) {
                val (title, value) = cmd.split('=')
                val box = boxes[title.hash()]
                val slot = box.slots.firstOrNull { it.title == title }
                if (slot != null) {
                    slot.value = value.toInt()
                } else {
                    box.slots += Slot(title, value.toInt())
                }
            } else {
                val title = cmd.dropLast(1)
                boxes[title.hash()].slots.removeIf { it.title == title }
            }
        }
        return boxes.withIndex().sumOf { (it.index + 1) * it.value.power() }
    }

    val testInput = readInputText("Day15_test")
    check(part1("HASH"), 52)
    check(part1(testInput), 1320)
    check(part2(testInput), 145)

    val input = readInputText("Day15")
    part1(input).println()
    part2(input).println()
}