package ru.bitc.totdesigner.platfom.adapter.state

/*
 * Created on 2019-12-09
 * @author YWeber
 */
sealed class QuestItem

data class FreeCardQuestItem(
        val name: String,
        val url: String
) : QuestItem()

data class PaidCardQuestItem(
        val name: String,
        val url: String
) : QuestItem()

data class HeaderItem(
        val title: String,
        val description: String,
        val lastSearchText: String
) : QuestItem()

data class TitleQuestItem(val title: String) : QuestItem()
data class ButtonQuestItem(val name: String) : QuestItem()

