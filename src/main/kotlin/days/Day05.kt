package days

import kotlin.math.max
import kotlin.math.min

class Day05() : genericDay(5) {
    data class Range(
        val lower: Long,
        val upper: Long
    ) {
        fun intersect(other: Range): Range? {
            if (max(lower, other.lower) > min(upper, other.upper)) return null
            return Range(max(lower, other.lower), min(other.upper, other.upper))
        }
    }

    data class RangeMap(
        val keyRange: Range,
        val destStart: Long
    ) {
        fun mapRange(source: Range): Pair<Range?, List<Range>> {
            val intersection = keyRange.intersect(source) ?: return null to listOf(source)
            val intersectionOffset = intersection.lower - keyRange.lower
            val intersectionRange = Range(destStart + intersectionOffset, destStart + intersectionOffset + intersection.upper - intersection.lower)

            val unmappedRanges = mutableListOf<Range>()
            if (source.lower < intersection.lower) {
                unmappedRanges.add(Range(source.lower, intersection.lower - 1))
            }
            if (source.upper > intersection.upper) {
                unmappedRanges.add(Range(intersection.upper + 1, source.upper))
            }

            return intersectionRange to unmappedRanges
        }
    }

    data class DataStore(
            val seedToSoilMap: List<RangeMap>,
            val soilToFertilizerMap: List<RangeMap>,
            val fertilizerToWaterMap: List<RangeMap>,
            val waterToLightMap: List<RangeMap>,
            val lightToTemperatureMap: List<RangeMap>,
            val temperatureToHumidityMap: List<RangeMap>,
            val humidityToLocationMap: List<RangeMap>,
    )
    override fun solvePartOne(input: String): String {
        val sections = input.split("\n\n")
        val datastore = buildDataStore(sections)
        
        return sections
            .first { it.startsWith("seeds:") }
            .split(" ")
            .drop(1)
            .map { Range(it.toLong(), it.toLong()) }
            .flatMap { findLocationForSeed(it, datastore) }
            .minBy { it.lower }
            .toString()
    }

    override fun solvePartTwo(input: String): String {
        val sections = input.split("\n\n")
        val datastore = buildDataStore(sections)

        val (seedsStarts, seedLengths) = sections
            .first { it.startsWith("seeds:") }
            .split(" ")
            .drop(1)
            .map { it.toLong() }
            .withIndex()
            .groupBy { it.index % 2 }
            .map { it.value.map { it.value } }
            .toList()

        return seedsStarts.zip(seedLengths)
                 .map { Range(it.first, it.first + it.second - 1) }
                .flatMap { findLocationForSeed(it, datastore) }
                .minBy { it.lower }
                .toString()
    }

    private fun findLocationForSeed(seed: Range, datastore: DataStore): List<Range> {
        val soil = findValueInPairList(seed, datastore.seedToSoilMap)
        val fertilizer = soil.flatMap { findValueInPairList(it, datastore.soilToFertilizerMap) }
        val water = fertilizer.flatMap { findValueInPairList(it, datastore.fertilizerToWaterMap) }
        val light = water.flatMap { findValueInPairList(it, datastore.waterToLightMap) }
        val temperature = light.flatMap { findValueInPairList(it, datastore.lightToTemperatureMap) }
        val humidity = temperature.flatMap { findValueInPairList(it, datastore.temperatureToHumidityMap) }
        return humidity.flatMap { findValueInPairList(it, datastore.humidityToLocationMap )}
    }

    private fun findValueInPairList(key: Long, pairList: List<Pair<LongRange, Long>>): Long {
        val entry = pairList.firstOrNull() { key in it.first }
        entry ?: return key

        return entry.second + key - entry.first.first
    }

    private fun findValueInPairList(key: Range, rangeMaps: List<RangeMap>): List<Range> {
        val (mapped, unmapped) = rangeMaps
            .fold(listOf<Range>() to listOf(key)) { acc, rangeMap ->
                val (mappedRanges, unmappedRanges) = acc
                val (newlyMappedRanges, newlyUnmappedRanges) = unmappedRanges
                    .map { rangeMap.mapRange(it) }
                    .unzip()

                mappedRanges + newlyMappedRanges.filterNotNull() to newlyUnmappedRanges.flatten()
            }
        return mapped + unmapped
    }

    private fun buildDataStore(sections: List<String>): DataStore {
        return DataStore(
            buildMapFromInputSection(sections.first {it.startsWith("seed-to-soil map:")}),
            buildMapFromInputSection(sections.first {it.startsWith("soil-to-fertilizer map:")}),
            buildMapFromInputSection(sections.first {it.startsWith("fertilizer-to-water map:")}),
            buildMapFromInputSection(sections.first {it.startsWith("water-to-light map:")}),
            buildMapFromInputSection(sections.first {it.startsWith("light-to-temperature map:")}),
            buildMapFromInputSection(sections.first {it.startsWith("temperature-to-humidity map:")}),
            buildMapFromInputSection(sections.first {it.startsWith("humidity-to-location map:")})
        )
    }

    private fun buildMapFromInputSection(input: String): List<RangeMap> {
        return input
                .lines()
                .drop(1)
                .map { buildMapFromInputLine(it) }
                .toList()
        
    }

    private fun buildMapFromInputLine(line: String): RangeMap {
        val (destRange, sourceRange, length) = line
                .split(" ")
                .map { it.toLong() }

        return RangeMap(Range(sourceRange, sourceRange + length - 1), destRange)
    }
}