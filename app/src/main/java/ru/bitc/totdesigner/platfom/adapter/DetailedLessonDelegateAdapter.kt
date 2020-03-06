package ru.bitc.totdesigner.platfom.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_detailed_description.*
import kotlinx.android.synthetic.main.item_detailed_title.*
import kotlinx.android.synthetic.main.item_image_preview.*
import kotlinx.android.synthetic.main.item_main_preview.*
import kotlinx.android.synthetic.main.item_preview.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.*
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadFileImage
import ru.bitc.totdesigner.system.setData

/**
 * Created on 05.03.2020
 * @author YWeber */

class DetailedLessonDelegateAdapter {

    fun createAdapter(selectItem: (Preview) -> Unit) =
        AsyncListDifferDelegationAdapter(
            DiffDetailedLessonItem,
            AdapterDelegatesManager<List<DetailedLessonItem>>()
                .addDelegate(mainPreviewAdapter())
                .addDelegate(galleryAdapter(selectItem))
                .addDelegate(titleAdapter())
                .addDelegate(descriptionAdapter())
        )

    private fun mainPreviewAdapter() =
        adapterDelegateLayoutContainer<Preview, DetailedLessonItem>(R.layout.item_main_preview) {
            bind {
                ivMainPreview.loadFileImage(item.path)
            }
        }

    private fun galleryAdapter(selectItem: (Preview) -> Unit) =
        adapterDelegateLayoutContainer<GalleryPreview, DetailedLessonItem>(R.layout.item_image_preview) {
            val adapter = AsyncListDifferDelegationAdapter(DiffDetailedLessonItem, innerPreviewAdapter(selectItem))
            rvOtherPreview.adapter = adapter
            rvOtherPreview.addItemDecoration(GridPaddingItemDecoration(6))
            bind {
                adapter.setData(item.otherImages)
            }
        }

    private fun innerPreviewAdapter(selectItem: (Preview) -> Unit) =
        adapterDelegateLayoutContainer<Preview, DetailedLessonItem>(R.layout.item_preview) {
            itemView.click { selectItem.invoke(item) }
            bind {
                ivOtherPreview.loadFileImage(item.path)
            }
        }

    private fun titleAdapter() =
        adapterDelegateLayoutContainer<TitleDetailedItem, DetailedLessonItem>(R.layout.item_detailed_title) {
            bind {
                tvDetailedTitle.text = item.title
            }
        }

    private fun descriptionAdapter() =
        adapterDelegateLayoutContainer<DescriptionDetailedItem, DetailedLessonItem>(R.layout.item_detailed_description) {
            bind {
                tvDetailedDescription.text = item.description
            }
        }


    private object DiffDetailedLessonItem : DiffUtil.ItemCallback<DetailedLessonItem>() {
        override fun areItemsTheSame(oldItem: DetailedLessonItem, newItem: DetailedLessonItem): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DetailedLessonItem, newItem: DetailedLessonItem): Boolean {
            return oldItem == newItem
        }
    }
}