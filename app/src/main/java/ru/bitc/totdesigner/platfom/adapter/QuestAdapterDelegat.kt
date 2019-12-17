package ru.bitc.totdesigner.platfom.adapter

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_button.*
import kotlinx.android.synthetic.main.item_quest.*
import kotlinx.android.synthetic.main.item_title.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.ButtonQuestItem
import ru.bitc.totdesigner.platfom.adapter.state.CardQuestItem
import ru.bitc.totdesigner.platfom.adapter.state.QuestItem
import ru.bitc.totdesigner.platfom.adapter.state.TitleQuestItem

/*
 * Created on 2019-12-09
 * @author YWeber
 */
class QuestAdapterDelegate() {

    fun createDelegate(click: (QuestItem) -> Unit): AdapterDelegatesManager<List<QuestItem>> =
        AdapterDelegatesManager<List<QuestItem>>()
            .addDelegate(questAdapter(click))
            .addDelegate(titleAdapter(click))
            .addDelegate(buttonItemAdapter(click))

    private fun titleAdapter(click: (QuestItem) -> Unit) =
        adapterDelegateLayoutContainer<TitleQuestItem, QuestItem>(R.layout.item_title) {
            itemView.setOnClickListener { click(item) }
            bind {
                tvTitleQuest.text = item.title
            }
        }

    private fun buttonItemAdapter(click: (QuestItem) -> Unit) =
        adapterDelegateLayoutContainer<ButtonQuestItem, QuestItem>(R.layout.item_button) {
            btnScrollStart.setOnClickListener { click(item) }
        }

    private fun questAdapter(click: (QuestItem) -> Unit) =
        adapterDelegateLayoutContainer<CardQuestItem, QuestItem>(R.layout.item_quest) {
            itemView.setOnClickListener { click(item) }
            bind {
                tvNameQuest.text = item.name
                Glide.with(context)
                    .load(item.url)
                    .into(ivImageQuest)
            }

        }


}