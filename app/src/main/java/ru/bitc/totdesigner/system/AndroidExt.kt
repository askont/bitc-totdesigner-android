package ru.bitc.totdesigner.system

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter

/*
 * Created on 2019-11-27
 * @author YWeber
 */

fun <V : View> V.click(block: (T: View) -> Unit) = setOnClickListener { block(it) }

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.toggleVisibility(): View {
    visibility = if (visibility == View.VISIBLE) View.INVISIBLE else View.INVISIBLE
    return this
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return context.inflater.inflate(layoutRes, this, false)
}

fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

inline val Context.displayWidth: Int
    get() = displayMetricks.widthPixels

inline val Context.displayHeight: Int
    get() = displayMetricks.heightPixels

inline val Context.displayMetricks: DisplayMetrics
    get() = resources.displayMetrics

inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun <T> AbsDelegationAdapter<T>.setData(data: T) {
    items = data
    notifyDataSetChanged()
}

fun ImageView.loadImage(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}


