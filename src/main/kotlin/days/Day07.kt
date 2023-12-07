package days

class Day07() : genericDay(7) {
    enum class HandValue {
        HIGH,
        PAIR,
        TWO_PAIR,
        THREE,
        FULL_HOUSE,
        FOUR,
        FIVE
    }
    data class Hand(
        val cards: List<Int>,
        val bid: Int
    ): Comparable<Hand> {
        fun getValue(): HandValue {
            val distinctCount = cards.distinct().count()

            if (cards.count { it == cards[0]} == 5) return HandValue.FIVE
            if (cards.count { it == cards[0]} == 4 || cards.count { it == cards[1]} == 4) return HandValue.FOUR
            if (distinctCount == 2) return HandValue.FULL_HOUSE
            if (cards.groupBy { it }.count() == 3 && cards.groupBy { it }.values.sortedByDescending { it.count() }[0].count() == 3) return HandValue.THREE
            if (distinctCount == 3) return HandValue.TWO_PAIR
            if (distinctCount == 4) return HandValue.PAIR
            return HandValue.HIGH
        }

        fun getValuePart2(): HandValue {
            val jokerlessHand = cards.filter { it != 0 }
            val jokers = cards.count { it == 0 }
            val distinctCount = jokerlessHand.distinct().count()

            if (jokerlessHand.count() <= 1 || jokerlessHand.count { it == jokerlessHand[0]} == 5 - jokers ) return HandValue.FIVE
            if (jokerlessHand.count { it == jokerlessHand[0]} == 4 - jokers || jokerlessHand.count { it == jokerlessHand[1]} == 4 - jokers) return HandValue.FOUR
            if (distinctCount == 2) return HandValue.FULL_HOUSE
            if (distinctCount == 3 && jokerlessHand.groupBy { it }.values.sortedByDescending { it.count() }[0].count() == 3 - jokers) return HandValue.THREE
            if (distinctCount == 3) return HandValue.TWO_PAIR
            if (distinctCount == 4) return HandValue.PAIR
            return HandValue.HIGH
        }

        fun compareToPart1(other: Hand): Int {
            val valueComparison = getValue().ordinal - other.getValue().ordinal
            if (valueComparison != 0) {
                return valueComparison
            }

            cards
                .zip(other.cards)
                .forEach{(a,b) ->
                    if (a - b != 0) {
                        return a - b
                    }
                }

            return 0
        }

        fun compareToPart2(other: Hand): Int {
            val valueComparison = getValuePart2().ordinal - other.getValuePart2().ordinal
            if (valueComparison != 0) {
                return valueComparison
            }

            cards
                .zip(other.cards)
                .forEach{(a,b) ->
                    if (a - b != 0) {
                        return a - b
                    }
                }

            return 0
        }

        override fun compareTo(other: Hand): Int {
            return compareToPart2(other)
        }

        companion object {
            fun fromInput(input: String): Hand {
                val (hand, bid) = input.split(" ")
                return Hand(
                    hand.map {
                        when (it) {
                            'A' -> 14
                            'K' -> 13
                            'Q' -> 12
                            'J' -> 11
                            'T' -> 10
                            else -> it.toString().toInt()
                        }
                    },
                    bid.toInt()
                )
            }

            fun fromInputPart2(input: String): Hand {
                val (hand, bid) = input.split(" ")
                return Hand(
                    hand.map {
                        when (it) {
                            'A' -> 14
                            'K' -> 13
                            'Q' -> 12
                            'J' -> 0
                            'T' -> 10
                            else -> it.toString().toInt()
                        }
                    },
                    bid.toInt()
                )
            }
        }
    }

    override fun solvePartOne(input: String): String {
        return input
            .lines()
            .map { Hand.fromInput(it) }
            .sortedDescending()
            .mapIndexed { index, it -> it.bid * (input.lines().count() - index)}
            .sum()
            .toString()
    }

    override fun solvePartTwo(input: String): String {
        return input
            .lines()
            .map { Hand.fromInputPart2(it) }
            .sortedDescending()
            .mapIndexed { index, it -> it.bid * (input.lines().count() - index)}
            .sum()
            .toString()
    }
}