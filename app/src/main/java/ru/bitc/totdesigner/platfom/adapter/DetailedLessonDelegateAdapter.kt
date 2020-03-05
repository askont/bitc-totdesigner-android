package ru.bitc.totdesigner.platfom.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_image_gallery.*
import kotlinx.android.synthetic.main.item_preview.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.DescriptionDetailedItem
import ru.bitc.totdesigner.platfom.adapter.state.DetailedLessonItem
import ru.bitc.totdesigner.platfom.adapter.state.ImageGalleryDetailedItem
import ru.bitc.totdesigner.platfom.adapter.state.TitleDetailedItem
import ru.bitc.totdesigner.system.loadFileImage
import ru.bitc.totdesigner.system.setData

/**
 * Created on 05.03.2020
 * @author YWeber */

class DetailedLessonDelegateAdapter {

    fun createAdapter() =
        AsyncListDifferDelegationAdapter(
            DiffDetailedLessonItem,
            AdapterDelegatesManager<List<DetailedLessonItem>>()
                .addDelegate(galleryAdapter { })
                .addDelegate(titleAdapter())
                .addDelegate(descriptionAdapter())
        )

    private fun galleryAdapter(selectItem: (ImageGalleryDetailedItem.Image) -> Unit) =
        adapterDelegateLayoutContainer<ImageGalleryDetailedItem, DetailedLessonItem>(R.layout.item_image_gallery) {
            val adapter = ListDelegationAdapter(innerPreviewAdapter(selectItem))
            rvOtherPreview.adapter = adapter
            bind {
                adapter.setData(item.otherImages)
                ivMainPreview.loadFileImage(item.mainImage)
            }
        }

    private fun innerPreviewAdapter(selectItem: (ImageGalleryDetailedItem.Image) -> Unit) =
        adapterDelegateLayoutContainer<ImageGalleryDetailedItem.Image, ImageGalleryDetailedItem.Image>(R.layout.item_preview) {
            bind {
                ivPreview.loadFileImage(item.image)
            }
        }

    private fun titleAdapter() =
        adapterDelegateLayoutContainer<TitleDetailedItem, DetailedLessonItem>(-1) {

        }

    private fun descriptionAdapter() =
        adapterDelegateLayoutContainer<DescriptionDetailedItem, DetailedLessonItem>(-1) {

        }


    private object DiffDetailedLessonItem : DiffUtil.ItemCallback<DetailedLessonItem>() {
        override fun areItemsTheSame(oldItem: DetailedLessonItem, newItem: DetailedLessonItem): Boolean = false

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DetailedLessonItem, newItem: DetailedLessonItem): Boolean {
            return oldItem == newItem
        }
    }
}