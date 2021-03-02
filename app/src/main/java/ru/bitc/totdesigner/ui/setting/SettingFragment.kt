package ru.bitc.totdesigner.ui.setting

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_setting.*
import org.koin.android.ext.android.inject
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActionFragment
import ru.bitc.totdesigner.platfom.adapter.setting.SettingDelegateAdapter
import ru.bitc.totdesigner.platfom.adapter.state.PriceSubscription
import ru.bitc.totdesigner.platfom.adapter.state.SettingItem
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.ui.setting.state.SettingState

/**
 * Created on 05.08.2020
 * @author YWeber
 * */

class SettingFragment :
    BaseActionFragment<SettingState, SettingViewModel>(R.layout.fragment_setting) {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private val adapter by lazy {
        SettingDelegateAdapter(
            ::handleClick,
            ::handleClickCard
        ).create()
    }

    override val viewModel: SettingViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvBackground.adapter = adapter
        rvBackground.addItemDecoration(TopBottomSpaceDecorator(8, 8))

    }

    override fun renderState(state: SettingState) {
        adapter.setData(state.items)

    }

    private fun handleClickCard(itemCard: PriceSubscription) {
        // TODO подписка
    }

    private fun handleClick(item: SettingItem) {
        viewModel.handleItemClick(item)
    }
}