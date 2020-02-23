package ru.bitc.totdesigner.ui.loading

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_loading_detailed.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.LoadingAdapterDelegate
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailedState

/**
 * Created on 04.02.2020
 * @author YWeber */

class LoadingDetailedFragment : BaseFragment(R.layout.fragment_loading_detailed) {

    private val viewModel by viewModel<LoadingDetailedViewModel>()

    private val loadingAdapter by lazy {
        LoadingAdapterDelegate().createDelegate(viewModel::userEvent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribe(viewModel.state, ::handleState)
    }

    private fun setupView() {
        rvLoading.adapter = loadingAdapter
        rvLoading.addItemDecoration(TopBottomSpaceDecorator())
        tvBackToCatalog.click { viewModel.backTo() }
        tvClearList.click { viewModel.clearDoneAndError() }
        tvCancelAll.click { viewModel.cancelAll() }
    }

    private fun handleState(loadingDetailedState: LoadingDetailedState) {
        loadingAdapter.setData(loadingDetailedState.loadingMiniItems)
    }

    override fun onBackPressed(): Boolean {
        viewModel.backTo()
        return true
    }

    override fun onDestroyView() {
        viewModel.onVisible()
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = with(LoadingDetailedFragment()) {
            this
        }
    }
}