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
    private val includeEdge: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacing
        outRect.right = spacing
        outRect.top = spacing
        outRect.bottom = spacing

        /*outRect.left = column * spacing / spanCount
        outRect.right =
            spacing - (column + 1) * spacing / spanCount
        if (position >= spanCount) {
            outRect.top = spacing // item top
        }*/
        /* if (includeEdge) {
             outRect.left =
                 spacing - (column - 1) * spacing / spanCount
             outRect.right =
                 (column) * spacing / spanCount

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
         }*/
    }
}
