package days

class Day01() : genericDay(1) {
    val textToIntegerMap = mapOf(
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

    override fun solvePartOne(input: String): String {
        return input
            .split("\n")
            .map { line -> line.first { it.isDigit() }.toString() + line.last { it.isDigit() }.toString() }
            .sumOf { it.toInt() }
            .toString()
    }

    override fun solvePartTwo(input: String): String {
        return input
            .split("\n")
            .map { replaceStringNumbers(it) }
            .sumOf { it.toInt() }
            .toString()
    }

    private fun replaceStringNumbers(line: String): String {
        val result = Regex("(${textToIntegerMap.keys.joinToString("|")}|${textToIntegerMap.values.joinToString("|")})")
            .findAll(line)
        val firstResult = result.first()
        val lastResult = result.last()

        return stringToIntIfNecessary(firstResult) + stringToIntIfNecessary(lastResult)
    }

    private fun stringToIntIfNecessary(firstResult: MatchResult) =
        if (firstResult.value.length > 1) textToIntegerMap[firstResult.value].toString() else firstResult.value
}