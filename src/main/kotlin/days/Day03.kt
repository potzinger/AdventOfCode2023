package days

class Day03() : genericDay(3) {
    private val partNumberRegex = Regex("(\\d+)")

    data class Location(
            val x: IntRange,
            val y: Int,
    ) {
        fun isNextTo(x: Int, y: Int): Boolean =
                this.y in (y - 1)..(y + 1) && this.x.first <= x + 1 && x - 1 <= this.x.last
    }

    data class PartNumber(
            val number: Int,
            val location: Location
    ) {
        fun isNextTo(x: Int, y: Int): Boolean = location.isNextTo(x, y)

        companion object {
            fun fromMatchResult(yLocation: Int, matchResult: MatchResult): PartNumber {
                return PartNumber(
                        matchResult.value.toInt(),
                        Location(
                                matchResult.range, yLocation
                        )
                )

            }
        }
    }

    override fun solvePartOne(input: String): String {
        val partNumbers = input
                .split("\n")
                .mapIndexed { index, it -> Pair(index, partNumberRegex.findAll(it)) }
                .flatMap { (index, resultSequence) -> resultSequence.iterator().asSequence().map { result -> Pair(index, result) } }
                .map { (index, matchResult) -> PartNumber.fromMatchResult(index, matchResult) }

        val markerLocations = input
                .split("\n")
                .flatMapIndexed { rowIndex, line ->
                    line.mapIndexed { columnIndex, char ->
                        if (!char.isDigit() && char != '.') {
                            Pair(columnIndex, rowIndex)
                        } else {
                            null
                        }
                    }
                }.filterNotNull()

        return partNumbers
                .filter { partNumber -> markerLocations.any { markerLocation -> partNumber.isNextTo(markerLocation.first, markerLocation.second) } }
                .sumOf { it.number }
                .toString()
    }

    override fun solvePartTwo(input: String): String {
        val partNumbers = input
                .split("\n")
                .mapIndexed { index, it -> Pair(index, partNumberRegex.findAll(it)) }
                .flatMap { (index, resultSequence) -> resultSequence.iterator().asSequence().map { result -> Pair(index, result) } }
                .map { (index, matchResult) -> PartNumber.fromMatchResult(index, matchResult) }

        val markerLocations = input
                .split("\n")
                .flatMapIndexed { rowIndex, line ->
                    line.mapIndexed { columnIndex, char ->
                        if (char == '*') {
                            Pair(columnIndex, rowIndex)
                        } else {
                            null
                        }
                    }
                }.filterNotNull()

        return markerLocations
                .map { markerLocation -> partNumbers.filter { it.isNextTo(markerLocation.first, markerLocation.second) } }
                .filter { it.count() == 2 }
                .sumOf { it[0].number * it[1].number }
                .toString()
    }
}