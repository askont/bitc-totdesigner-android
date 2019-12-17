package ru.bitc.totdesigner.platfom.adapter.state

/*
 * Created on 2019-12-09
 * @author YWeber
 */
sealed class QuestItem

data class CardQuestItem(
    val name: String,
    val url: String
) : QuestItem()

data class TitleQuestItem(val title: String) : QuestItem()
data class ButtonQuestItem(val name: String) : QuestItem()

