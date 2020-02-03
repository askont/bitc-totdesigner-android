package ru.bitc.totdesigner.system

import android.content.res.Resources

/*
 * Created on 2019-12-09
 * @author YWeber
 */

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.dpToPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.dpToPxFloat(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Boolean.toInt(): Int = when (this) {
    true -> 1
    false -> 0
}
