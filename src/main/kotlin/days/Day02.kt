package days

import java.util.*

class Day02() : genericDay(2) {
    enum class Color {
        RED,
        GREEN,
        BLUE
    }
    
    data class Pick(
        val color: Color,
        val amount: Int
    ) {
        companion object {
            private val inputRegex = Regex("(\\d+) (\\w+)")
            fun fromInput(input: String): Pick {
                val matchResult = inputRegex.matchEntire(input)!!
                return Pick(
                        Color.valueOf(matchResult.groupValues[2].uppercase(Locale.getDefault())),
                        matchResult.groupValues[1].toInt())
            }
        }
    }

    data class GameSet (
            val picks: List<Pick>
    ) {
        fun getNumCubesOfColor(color: Color) = picks.filter { it.color == color }.sumOf { it.amount }
        companion object {
            fun fromInput(input: String): GameSet {
                return GameSet(input
                        .split(", ")
                        .map { Pick.fromInput(it) })
            }
        }
    }
    
    data class Game (
            val id: Int,
            val sets: List<GameSet>
    ) {
        private fun getMaxAmountOfColor(color: Color) = sets.maxOfOrNull { it.getNumCubesOfColor(color) } ?: 0
        fun isPossibleWith(numRed: Int, numGreen: Int, numBlue: Int): Boolean = getMaxAmountOfColor(Color.RED) <= numRed && getMaxAmountOfColor(Color.GREEN) <= numGreen && getMaxAmountOfColor(Color.BLUE) <= numBlue
        fun getPower(): Int =
                getMaxAmountOfColor(Color.RED) * getMaxAmountOfColor(Color.GREEN) * getMaxAmountOfColor(Color.BLUE)

        companion object {
            fun fromInput(input: String): Game {
                val splitInput = input.substringAfter("Game ").split(": ")
                
                return Game(
                        splitInput[0].toInt(),
                        splitInput[1]
                        .split("; ")
                        .map { GameSet.fromInput(it) })
            }
        }
    }
    
    override fun solvePartOne(input: String): String {
        return input
                .split("\n")
                .map { Game.fromInput(it) }
                .filter { it.isPossibleWith(12, 13, 14) }
                .sumOf { it.id }
                .toString()
    }

    override fun solvePartTwo(input: String): String {
        return input
                .split("\n")
                .map { Game.fromInput(it) }
                .sumOf { it.getPower() }
                .toString()
    }
}