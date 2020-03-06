package ru.bitc.totdesigner.platfom.adapter

import android.annotation.SuppressLint
import androidx.core.view.isVisible
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
import ru.bitc.totdesigner.system.loadFileImage

/**
 * Created on 04.03.2020
 * @author YWeber */

class HomeLessonDelegateAdapter {

    fun createAdapter(click: (HomeLessonItem) -> Unit) = AsyncListDifferDelegationAdapter(
        DiffHomeLessonItem,
        AdapterDelegatesManager<List<HomeLessonItem>>()
            .addDelegate(titleAdapter(click))
            .addDelegate(buttonItemAdapter(click))
            .addDelegate(savedLessonAdapter(click))
            .addDelegate(headerAdapter(click))
    )

    private fun headerAdapter(click: (HomeLessonItem) -> Unit) =
        adapterDelegateLayoutContainer<HeaderHomeLesson, HomeLessonItem>(R.layout.item_header_catalog) {
            tvDownloadDetailed.click { click(item) }
            bind {
                tvDownloadDetailed.isVisible = false
                tvHeaderTitle.text = item.title
                tvDescription.text = item.description
            }
        }

    private fun titleAdapter(click: (HomeLessonItem) -> Unit) =
        adapterDelegateLayoutContainer<TitleHomeLessonItem, HomeLessonItem>(R.layout.item_title) {
            itemView.setOnClickListener { click(item) }
            bind {
                tvTitleQuest.text = item.title
            }
        }

    private fun buttonItemAdapter(click: (HomeLessonItem) -> Unit) =
        adapterDelegateLayoutContainer<BottomHomeLessonItem, HomeLessonItem>(R.layout.item_button) {
            btnScrollStart.setOnClickListener { click(item) }
        }

    private fun savedLessonAdapter(click: (HomeLessonItem) -> Unit) =
        adapterDelegateLayoutContainer<SavedHomeLessonItem, HomeLessonItem>(R.layout.item_lesson) {
            itemView.setOnClickListener { click(item) }
            cvCardLesson.setOnClickListener { click(item) }
            bind {
                tvNameQuest.text = item.nameLesson
                ivImageQuest.loadFileImage(item.mainImageLocalPath)
            }
        }
}

private object DiffHomeLessonItem : DiffUtil.ItemCallback<HomeLessonItem>() {
    override fun areItemsTheSame(oldItem: HomeLessonItem, newItem: HomeLessonItem): Boolean =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HomeLessonItem, newItem: HomeLessonItem): Boolean =
        oldItem == newItem

}