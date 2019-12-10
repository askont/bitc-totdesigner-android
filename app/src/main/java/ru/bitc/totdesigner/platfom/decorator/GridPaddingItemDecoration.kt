package ru.bitc.totdesigner.platfom.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
 * Created on 2019-12-09
 * @author YWeber
 */
class GridPaddingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val excludeFirstItem: Boolean = true,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var position = parent.getChildAdapterPosition(view)
        if (excludeFirstItem && position == 0)
            return
        position += if (excludeFirstItem) 1 else 0
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount
            outRect.right =
                (column + 1) * spacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right =
                spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}
