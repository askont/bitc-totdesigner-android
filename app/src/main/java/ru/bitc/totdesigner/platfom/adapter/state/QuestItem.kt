package ru.bitc.totdesigner.platfom.adapter.state

/*
 * Created on 2019-12-09
 * @author YWeber
 */
sealed class QuestItem(val hashId: String)

data class FreeCardQuestItem(
    val name: String,
    val url: String
) : QuestItem(url)

data class PaidCardQuestItem(
    val name: String,
    val url: String
) : QuestItem(url)

data class HeaderItem(
    val title: String,
    val description: String,
    val lastSearchText: String
) : QuestItem(title + lastSearchText)

data class TitleQuestItem(val title: String) : QuestItem(title)
data class ButtonQuestItem(val name: String) : QuestItem(name)

