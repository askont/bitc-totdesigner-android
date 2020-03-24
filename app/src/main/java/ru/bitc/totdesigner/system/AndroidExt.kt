package ru.bitc.totdesigner.system

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateLayoutContainerViewHolder
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.BaseFragment
import timber.log.Timber
import java.io.File

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

fun ViewGroup.setDialogSize() {
    val defaultDisplay = (context as FragmentActivity).windowManager.defaultDisplay
    val point = Point()
    defaultDisplay.getSize(point)
    setPadding(
        (point.x * 0.2).toInt(),
        (point.x * 0.05).toInt(),
        (point.x * 0.2).toInt(),
        (point.x * 0.05).toInt()
    )
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

fun <T> AsyncListDifferDelegationAdapter<T>.setData(data: List<T>) {
    items = data
}

fun ImageView.loadImage(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.img_logotype)
        .into(this)
}

fun ImageView.loadFileImage(url: String) {
    Glide.with(context)
        .load(File(url))
        .into(this)
}

fun ImageView.loadFileImage(url: String, height: Int, width: Int) {
    Glide.with(context)
        .load(File(url))
        .override(width, height)
        .into(this)
}

inline fun <reified T> T.printDebug(message: String = "Test Debug"): T =
    this.apply {
        Timber.e("$message...$this")
    }

fun SearchView.querySearch(block: (String?) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            block.invoke(newText)
            return true
        }
    })
}

fun TextView.htmlText(html: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    } else {
        Html.fromHtml(html);
    }
}

fun BaseFragment.toast(message: CharSequence) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()
fun BaseActivity.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
fun AdapterDelegateLayoutContainerViewHolder<*>.toast(message: CharSequence) =
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()

