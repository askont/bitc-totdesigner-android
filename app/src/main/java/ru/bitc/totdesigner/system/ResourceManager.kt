package ru.bitc.totdesigner.system

import android.content.Context
import androidx.annotation.StringRes
import java.io.InputStream

/*
 * Created on 2019-12-09
 * @author YWeber
 */
class ResourceManager(private val context: Context) {

    fun getString(@StringRes resId: Int): String = context.resources.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String =
        context.resources.getString(resId, *formatArgs)

    fun getAsset(name: String): InputStream = context.resources.assets.open(name)
}