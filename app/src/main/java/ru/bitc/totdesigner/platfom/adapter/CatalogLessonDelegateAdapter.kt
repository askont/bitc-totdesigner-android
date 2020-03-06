package ru.bitc.totdesigner.platfom.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_button.*
import kotlinx.android.synthetic.main.item_header_catalog.*
import kotlinx.android.synthetic.main.item_lesson.*
import kotlinx.android.synthetic.main.item_title.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage

/*
 * Created on 2019-12-09
 * @author YWeber
 */
class LessonAdapterDelegate {

    fun createAdapter(click: (LessonItem) -> Unit): AsyncListDifferDelegationAdapter<LessonItem> =
        AsyncListDifferDelegationAdapter<LessonItem>(
            DiffLessonItem, AdapterDelegatesManager<List<LessonItem>>()
                .addDelegate(headerAdapter(click))
                .addDelegate(freeLessonAdapter(click))
                .addDelegate(titleAdapter(click))
                .addDelegate(buttonItemAdapter(click))
                .addDelegate(paidLessonAdapter(click))
        )

    private fun headerAdapter(click: (LessonItem) -> Unit) =
        adapterDelegateLayoutContainer<HeaderItem, LessonItem>(R.layout.item_header_catalog) {
            tvDownloadDetailed.click { click(item) }
            bind {
                tvHeaderTitle.text = item.title
                tvDescription.text = item.description
            }
        }

    private fun titleAdapter(click: (LessonItem) -> Unit) =
        adapterDelegateLayoutContainer<TitleLessonItem, LessonItem>(R.layout.item_title) {
            itemView.setOnClickListener { click(item) }
            bind {
                tvTitleQuest.text = item.title
            }
        }

    private fun buttonItemAdapter(click: (LessonItem) -> Unit) =
        adapterDelegateLayoutContainer<ButtonLessonItem, LessonItem>(R.layout.item_button) {
            btnScrollStart.setOnClickListener { click(item) }
        }

    private fun freeLessonAdapter(click: (LessonItem) -> Unit) =
        adapterDelegateLayoutContainer<FreeCardLessonItem, LessonItem>(R.layout.item_lesson) {
            itemView.setOnClickListener { click(item) }
            cvCardLesson.setOnClickListener { click(item) }
            bind {
                tvNameQuest.text = item.name
                ivImageQuest.loadImage(item.url)
            }
        }

    private fun paidLessonAdapter(click: (LessonItem) -> Unit) =
        adapterDelegateLayoutContainer<PaidCardLessonItem, LessonItem>(R.layout.item_lesson) {
            itemView.setOnClickListener { click(item) }
            cvCardLesson.setOnClickListener { click(item) }
            bind {
                tvNameQuest.text = item.name
                ivImageQuest.loadImage(item.url)
            }
        }

}

private object DiffLessonItem : DiffUtil.ItemCallback<LessonItem>() {
    override fun areItemsTheSame(oldItem: LessonItem, newItem: LessonItem): Boolean = oldItem.hashId == newItem.hashId

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LessonItem, newItem: LessonItem): Boolean = oldItem == newItem
}