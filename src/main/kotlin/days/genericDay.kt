package days

abstract class genericDay(val dayNum: Int) {
    abstract fun solvePartOne(input: String): String
    abstract fun solvePartTwo(input: String): String

    fun solve() {
        val input = getDayInput()
        println("Day ${String.format("%02d", dayNum)} - A: ${solvePartOne(input)} - B: ${solvePartTwo(input)}")
    }

    private fun getDayInput() =
        genericDay::class.java.getResource("/dayInputs/${String.format("%02d", dayNum)}.txt")!!.readText()
}
