package ru.bitc.totdesigner.ui.mock

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_mock.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment

/*
 * Created on 2019-12-03
 * @author YWeber
 */

private const val MESSAGE_EXTRA = "mock message"

class MockFragment : BaseFragment(R.layout.fragment_mock) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = arguments?.getString(MESSAGE_EXTRA) ?: ""
        tvMock.text = message
    }

    companion object {
        fun newInstance(message: String) = with(MockFragment()) {
            val bundle = Bundle()
            bundle.putString(MESSAGE_EXTRA, message)
            arguments = bundle
            this
        }
    }
}