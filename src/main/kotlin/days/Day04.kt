package days

import kotlin.math.pow

class Day04() : genericDay(4) {
    data class Game(
            val luchyNumbers: List<Int>,
            val numbers: List<Int>,
            val id: Int
    ) {
        fun getCntWinningNums() = numbers.count { it in luchyNumbers }
        fun getScore(): Double {
            val cntWinningNums = getCntWinningNums()
            if (cntWinningNums == 0) {
                return 0.0
            }
            return 2.0.pow(cntWinningNums - 1)
        }

        fun getBonusCards() = (id + 1 .. id + getCntWinningNums()).toList()

                companion object {
            fun fromInput(input: String, id: Int): Game {
                val (winningNumbers, numbers) = input.split("|")
                return Game(
                        winningNumbers
                                .trim()
                                .split(Regex(" +"))
                                .map { it.toInt() },
                        numbers
                                .trim()
                                .split(Regex(" +"))
                                .map { it.toInt() },
                        id
                )
            }
        }
    }
    override fun solvePartOne(input: String): String {
        return input
                .split("\n")
                .map { it.substringAfter(": ") }
                .mapIndexed {index, it -> Game.fromInput(it, index) }
                .sumOf { it.getScore() }
                .toString()
    }

    override fun solvePartTwo(input: String): String {
        val cardAmounts = MutableList(input.lines().count()) { 1 }
        val games = input
                .split("\n")
                .map { it.substringAfter(": ") }
                .mapIndexed {index, it -> Game.fromInput(it, index) }
        
        games.forEach {game ->
            game.getBonusCards().forEach { bonusCard ->
                cardAmounts[bonusCard] += cardAmounts[game.id]
            }
        }
        
        return cardAmounts.sumOf { it }
                .toString()
    }
}