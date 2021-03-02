package ru.bitc.totdesigner.ui.statistic.state

import ru.bitc.totdesigner.platfom.adapter.state.StatisticItem

data class StatisticState(
    val titleLesson: String,
    val imagePath: String,
    val stepDone: Int,
    val stepFailed: Int,
    val stepSkip: Int,
    val itemStatistic: List<StatisticItem>
) {
    companion object {
        val EMPTY = StatisticState("", "", 0, 0, 0, emptyList())
    }
}