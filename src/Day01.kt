fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            line -> 10 * line.first { it.isDigit() }.digitToInt() + line.last { it.isDigit() }.digitToInt()
        }
    }

    val digits = mutableMapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )
    for (digit in 1..9) {
        digits[digit.toString()] = digit
    }
    fun part2(input: List<String>): Int {
        fun Pair<Int, String>?.toDigit(): Int = digits[this!!.second]!!
        return input.sumOf {
            line -> 10 * line.findAnyOf(digits.keys).toDigit() + line.findLastAnyOf(digits.keys).toDigit()
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
