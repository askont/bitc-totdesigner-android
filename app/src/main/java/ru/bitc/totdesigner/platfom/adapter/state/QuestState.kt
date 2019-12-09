package ru.bitc.totdesigner.platfom.adapter.state

/*
 * Created on 2019-12-09
 * @author YWeber
 */
sealed class QuestState

data class CardQuestState(
    val name: String,
    val url: String
) : QuestState()