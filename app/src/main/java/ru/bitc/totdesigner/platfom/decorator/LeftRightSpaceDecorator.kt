package ru.bitc.totdesigner.platfom.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.bitc.totdesigner.system.dpToPx

class LeftRightSpaceDecorator(
    private val marginTop: Int = 2,
    private val marginBottom: Int = 2
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = marginBottom.dpToPx()
        outRect.right = marginTop.dpToPx()
    }
}