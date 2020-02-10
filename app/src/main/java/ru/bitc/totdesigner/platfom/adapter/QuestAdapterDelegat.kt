package ru.bitc.totdesigner.platfom.adapter

import androidx.core.widget.addTextChangedListener
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_button.*
import kotlinx.android.synthetic.main.item_quest.*
import kotlinx.android.synthetic.main.item_search_header.*
import kotlinx.android.synthetic.main.item_title.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.system.loadImage

/*
 * Created on 2019-12-09
 * @author YWeber
 */
class QuestAdapterDelegate {

    fun createDelegate(click: (QuestItem) -> Unit, search: (String) -> Unit): AdapterDelegatesManager<List<QuestItem>> =
        AdapterDelegatesManager<List<QuestItem>>()
            .addDelegate(searchHeaderAdapter(search))
            .addDelegate(freeQuestAdapter(click))
            .addDelegate(titleAdapter(click))
            .addDelegate(buttonItemAdapter(click))
            .addDelegate(paidQuestAdapter(click))


    private fun searchHeaderAdapter(searchText: (search: String) -> Unit) =
        adapterDelegateLayoutContainer<SearchHeaderItem, QuestItem>(R.layout.item_search_header) {
            inputTextSearch.addTextChangedListener {
                if (it == null) return@addTextChangedListener
                searchText.invoke(it.toString())
            }
            bind {
                tvTitle.text = item.title
                tvDescription.text = item.description
            }
        }

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

    private fun freeQuestAdapter(click: (QuestItem) -> Unit) =
        adapterDelegateLayoutContainer<FreeCardQuestItem, QuestItem>(R.layout.item_quest) {
            itemView.setOnClickListener { click(item) }
            bind {
                tvNameQuest.text = item.name
                ivImageQuest.loadImage(item.url)
            }
        }

    private fun paidQuestAdapter(click: (QuestItem) -> Unit) =
        adapterDelegateLayoutContainer<PaidCardQuestItem, QuestItem>(R.layout.item_quest) {
            itemView.setOnClickListener { click(item) }
            bind {
                tvNameQuest.text = item.name
                ivImageQuest.loadImage(item.url)
            }
        }


}