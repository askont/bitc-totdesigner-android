package ru.bitc.totdesigner.model.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import ru.bitc.totdesigner.R

/**
 * Created on 12.08.2020
 * @author YWeber */

interface PrefsStore {
    var currentBackground: Int
}

class AndroidPrefsStore(private val context: Context) : PrefsStore {

    companion object {
        private const val PREF_CURRENT_BACKGROUND = "PREF CURRENT BACKGROUND"
    }

    private val preference by lazy { context.getSharedPreferences("appPref", MODE_PRIVATE) }

    override var currentBackground: Int
        get() = preference.getInt(PREF_CURRENT_BACKGROUND, R.drawable.img_totdesign)
        set(value) {
            preference.edit {
                putInt(PREF_CURRENT_BACKGROUND, value)
                    .apply()
            }
        }
}