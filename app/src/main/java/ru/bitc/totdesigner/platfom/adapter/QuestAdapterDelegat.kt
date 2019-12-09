package ru.bitc.totdesigner.platfom.adapter

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_quest.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.CardQuestState
import ru.bitc.totdesigner.platfom.adapter.state.QuestState

/*
 * Created on 2019-12-09
 * @author YWeber
 */
class QuestAdapterDelegate {

    fun createDelegate(): AdapterDelegatesManager<List<QuestState>> = AdapterDelegatesManager<List<QuestState>>()
        .addDelegate(questAdapter())

    private fun questAdapter() =
        adapterDelegateLayoutContainer<CardQuestState, QuestState>(R.layout.item_quest) {
            bind {
                tvNameQuest.text = item.name
                Glide.with(context)
                    .load(item.url)
                    .into(ivImageQuest)
            }

        }


}