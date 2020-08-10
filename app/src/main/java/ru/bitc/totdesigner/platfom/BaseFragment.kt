package ru.bitc.totdesigner.platfom

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/*
 * Created on 2019-11-25
 * @author YWeber
 */
abstract class BaseFragment(@LayoutRes val layoutRes: Int) : Fragment(layoutRes) {
    // return true if fragment need custom navigation
    open fun onBackPressed(): Boolean = false
}