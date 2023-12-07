package days

class Day06() : genericDay(6) {
    override fun solvePartOne(input: String): String {
        val (timeInput, distanceInput) = input.lines()
        val times = timeInput.split(Regex(" +")).drop(1).map { it.toLong() }
        val distances = distanceInput.split(Regex(" +")).drop(1).map { it.toLong() }

        val raceInput = times.zip(distances)
        return raceInput
            .map { getNumWinnigInputs(it.first, it.second) }
            .reduce{ a, b -> a * b }
            .toString()
    }

    private fun getNumWinnigInputs(time: Long, distance: Long): Int =
        (0..time).count { it * (time - it) > distance }

    override fun solvePartTwo(input: String): String {
        return solvePartOne(input)
    }
}