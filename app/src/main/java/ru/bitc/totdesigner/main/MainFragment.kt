package ru.bitc.totdesigner.main

import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment

/*
 * Created on 2019-12-01
 * @author YWeber
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {




    companion object {
        fun newInstance() = with(MainFragment()) {
            this
        }
    }
}