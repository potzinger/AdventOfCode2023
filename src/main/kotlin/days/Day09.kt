package days

class Day09() : genericDay(9) {
    override fun solvePartOne(input: String): String {
        return input.
                lines()
                .map { it.split(Regex(" +")).map { it.toLong() } }
                .map { buildHierarchyForHistoryInput(it) }
                .sumOf { it.first().last() }
                .toString()
    }

    override fun solvePartTwo(input: String): String {
        return input.
        lines()
                .map { it.split(Regex(" +")).map { it.toLong() } }
                .map { it.reversed() }
                .map { buildHierarchyForHistoryInput(it) }
                .sumOf { it.first().last() }
                .toString()
    }

    private fun buildHierarchyForHistoryInput(historyInput: List<Long>): List<List<Long>> {
        val workList = listOf(historyInput.toMutableList()).toMutableList()
        
        while (workList.last().all { it == 0L }.not()) {
            workList.add(buildHistoryForLayer(workList.last()))
        }
        
        return extrapolateLayers(workList)
    }

    private fun buildHistoryForLayer(historyInput: List<Long>): MutableList<Long> {
        return historyInput
                .zipWithNext { a, b -> (b - a) }.toMutableList()
    }

    private fun extrapolateLayers(workList: MutableList<MutableList<Long>>): MutableList<MutableList<Long>> {
        workList.last().add(0) //Last layer is always all zeros
        
        (workList.count() - 2 downTo 0).forEach { layerIndex ->
            workList[layerIndex].add(workList[layerIndex + 1].last() + workList[layerIndex].last())
        }
        
        return workList
    }
}