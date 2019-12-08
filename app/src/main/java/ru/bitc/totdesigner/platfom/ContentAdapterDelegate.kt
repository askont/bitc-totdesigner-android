package ru.bitc.totdesigner.platfom

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContentAdapterDelegate {
    private var viewType: Int = 0

    constructor(vt:Int) {
        viewType = vt
    }

    fun getViewType(): Int {
        return viewType
    }

//    fun isForViewType(items: List, position: Int): Boolean

//    fun onCreateViewHolder(parent: ViewGroup) {
//        return
//    }

}

interface  AdapterDelegate<T> {

    fun isForViewType(items: T, position: Int): Boolean

    fun onCreateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder

    fun onBindViewHolder(items: T, positin: Int, holder: RecyclerView.ViewHolder)
}