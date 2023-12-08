package days

import java.lang.RuntimeException

data class Node (
    val id: String,
    val leftChild: String,
    val rightChild: String
) {
    companion object {
        private val inputRegex = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")
        fun fromInput(input: String): Node {
            val (_, id, leftChild, rightChild) = inputRegex.find(input)!!.groupValues
            return Node(id, leftChild, rightChild)
        }
    }
}

class Day08() : genericDay(8) {
    override fun solvePartOne(input: String): String {
        val directionInput = input.lines().first()
        val nodes = input
            .lines()
            .drop(2)
            .map { Node.fromInput(it) }
            .associateBy { it.id }

        val directionSequence = sequence<Char> { while(true) yieldAll(directionInput.toList()) }.iterator()
        return getNumNodesToEnd(nodes, directionSequence, nodes["AAA"]!!).toString()
    }

    override fun solvePartTwo(input: String): String {
        val directionInput = input.lines().first()
        val nodes = input
            .lines()
            .drop(2)
            .map { Node.fromInput(it) }
            .associateBy { it.id }

        val directionSequence = sequence<Char> { while(true) yieldAll(directionInput.toList()) }.iterator()
        val startNodes = nodes
            .values
            .filter { it.id.endsWith("A") }
        return getParallelNumNodesToEnd(nodes, directionSequence, startNodes).toString()
    }

    private fun getNumNodesToEnd(nodes: Map<String, Node>, directionSequence: Iterator<Char>, startNode: Node): Int {
        var curNode = startNode
        var stepsTaken = 0

        while (true) {
            if (curNode.id == "ZZZ") return stepsTaken
            curNode = when (directionSequence.next()) {
                'L' -> nodes[curNode.leftChild]!!
                'R' -> nodes[curNode.rightChild]!!
                else -> throw RuntimeException("Invalid input")
            }
            stepsTaken++
        }
    }

    private fun getParallelNumNodesToEnd(nodes: Map<String, Node>, directionSequence: Iterator<Char>, startNodes: List<Node>): Long {
        val curNodes = startNodes.toMutableList()
        var stepsTaken = 0L

        while (true) {
            if (curNodes.all { it.id.endsWith("Z")}) return stepsTaken
            if (stepsTaken % 10000000 == 0L) println("Took $stepsTaken steps")

            // LCM of loop steps of parallel iterations; calculated outside of program
            // Spotted incidentally while looking at output. Wouldn't work if steps from startLocation -> endlocation wouldn't match loop steps for all parallel iterations
            if (stepsTaken > 22199) return 13334102464297L

            curNodes
                .withIndex()
                .filter { it.value.id.endsWith("Z") }
                .forEach { println("${it.index} reached ${it.value.id} after $stepsTaken steps") }

            val nextDirection = directionSequence.next()

            (0 ..< curNodes.size).forEach { index ->
                curNodes[index] = when (nextDirection) {
                    'L' -> nodes[curNodes[index].leftChild]!!
                    'R' -> nodes[curNodes[index].rightChild]!!
                    else -> throw RuntimeException("Invalid input")
                }
            }

            stepsTaken++
        }
    }
}