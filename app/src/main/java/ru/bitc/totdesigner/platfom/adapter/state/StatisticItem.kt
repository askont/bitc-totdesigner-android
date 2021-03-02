package ru.bitc.totdesigner.platfom.adapter.state

data class StatisticItem(
    val isDone: Boolean,
    val titleLesson: String,
    val subtitleStep: String,
    val percent: String,
    val countStepInMaxStep: String
)