package ru.bitc.totdesigner.platfom.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.bitc.totdesigner.system.dpToPx

/**
 * Created on 19.02.2020
 * @author YWeber */

class TopBottomSpaceDecorator(
    private val marginTop: Int = 2.dpToPx(),
    private val marginBottom: Int = 2.dpToPx()
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = marginBottom
        outRect.top = marginTop
    }
}