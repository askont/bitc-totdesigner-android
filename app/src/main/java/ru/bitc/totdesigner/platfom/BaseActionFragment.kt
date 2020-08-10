package ru.bitc.totdesigner.platfom

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.observe

/**
 * Created on 05.08.2020
 * @author YWeber */

abstract class BaseActionFragment<ST, VM : BaseActionViewModel<ST>>(@LayoutRes layoutRes: Int) :
    BaseFragment(layoutRes) {

    abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    abstract fun renderState(state: ST)
}