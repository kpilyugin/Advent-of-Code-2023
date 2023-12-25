import kotlin.random.Random

fun main() {
    class Node(val name: String) {
        val next = mutableListOf<String>()
    }

    fun part1(input: List<String>): Int {
        val nodes = HashMap<String, Node>()
        input.forEach {
            val from = it.substringBefore(':')
            val to = it.substringAfter(": ").split(" ")
            nodes.getOrPut(from) { Node(from) }.next.addAll(to)
            for (out in to) {
                nodes.getOrPut(out) { Node(out) }.next.add(from)
            }
        }
        fun Pair<String, String>.toKey(): Pair<String, String> {
            val small = if (first < second) first else second
            val big = if (first > second) first else second
            return small to big
        }
        val frequencies = HashMap<Pair<String, String>, Int>()
        fun walk(v: Node, to: Node, visited: HashSet<String>, path: ArrayList<String>) {
            path.add(v.name)
            visited.add(v.name)
            if (v == to) {
                path.zipWithNext().forEach {
                    val key = it.toKey()
                    frequencies[key] = (frequencies[key] ?: 0) + 1
                }
                return
            }
            v.next.shuffled().forEach {
                if (it !in visited) {
                    walk(nodes[it]!!, to, visited, path)
                }
            }
            path.removeLast()
        }

        val nodesList = nodes.values.toList()
        repeat(2000) {
            val to = Random.nextInt(1, nodesList.size)
            val from = Random.nextInt(to)
            walk(nodesList[from], nodesList[to], HashSet(), ArrayList())
        }
        val mostOften = frequencies.entries.sortedByDescending { it.value }.take(5)
        mostOften.forEach { entry ->
            println("${entry.key}: ${entry.value}")
        }
        val toRemove = mostOften.take(3).map { it.key }.toSet()
        val component = HashSet<String>()
        fun dfs(v: Node) {
            component.add(v.name)
            v.next.forEach {
                if (it !in component && (v.name to it).toKey() !in toRemove) {
                    dfs(nodes[it]!!)
                }
            }
        }
        dfs(nodes.values.first())
        return component.size * (nodes.size - component.size)
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput), 54)

    val input = readInput("Day25")
    println(part1(input))
}


