package ru.bitc.totdesigner.platfom.adapter.ineraction

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_interaction_preview.*
import kotlinx.android.synthetic.main.item_part_image.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.InteractionPartItem
import ru.bitc.totdesigner.platfom.drag.ScaleDragShadowBuilder
import ru.bitc.totdesigner.platfom.drag.dragView
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadFileImage
import ru.bitc.totdesigner.system.toast

/**
 * Created on 11.03.2020
 * @author YWeber */

class InteractionPartDelegateAdapter {

    fun createAdapter(click: (InteractionPartItem) -> Unit) =
        AsyncListDifferDelegationAdapter(
            DiffInteractionPart,
            AdapterDelegatesManager<List<InteractionPartItem>>()
                .addDelegate(partAdapter())
                .addDelegate(previewAdapter(click))
        )

    private fun previewAdapter(click: (InteractionPartItem) -> Unit) =
        adapterDelegateLayoutContainer<InteractionPartItem.Preview, InteractionPartItem>(R.layout.item_interaction_preview) {
            cvCardPreview.click { click(item) }
            bind {
                val layoutParams = cvCardPreview.layoutParams
                if (item.isSelect) {
                    layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
                    layoutParams.width = context.resources.getDimension(R.dimen.width_select_preview).toInt()
                } else {
                    layoutParams.height = context.resources.getDimension(R.dimen.height_preview).toInt()
                    layoutParams.width = context.resources.getDimension(R.dimen.width_preview).toInt()
                }
                cvCardPreview.layoutParams = layoutParams
                ivPreview.loadFileImage(item.path)
            }
        }

    private fun partAdapter() =
        adapterDelegateLayoutContainer<InteractionPartItem.Part, InteractionPartItem>(R.layout.item_part_image) {
            containerPartImage.setOnLongClickListener {
                if (item.isPermissionDrop) {
                    containerPartImage.background = ContextCompat.getDrawable(context, R.drawable.bg_select_part)
                    val dragDate = ScaleDragShadowBuilder.createDate(
                        item.id,
                        itemX = "-",
                        itemY = "-"
                    )
                    val dragImg = ScaleDragShadowBuilder(ivPartImage, item.height, item.height)
                    it.dragView(dragDate, dragImg)
                } else {
                    toast("Для перемещения элементов необходимо запустить урок")
                }
                true
            }
            bind {
                tvNamePart.text = item.name
                ivPartImage.loadFileImage(item.path)
                if (!item.isPermissionDrop) {
                    containerPartImage.background = ContextCompat.getDrawable(context, R.drawable.bg_select_part_image)
                } else {
                    containerPartImage.background = null
                }
            }
        }

    private object DiffInteractionPart : DiffUtil.ItemCallback<InteractionPartItem>() {
        override fun areItemsTheSame(oldItem: InteractionPartItem, newItem: InteractionPartItem): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: InteractionPartItem, newItem: InteractionPartItem): Boolean =
            oldItem == newItem
    }
}