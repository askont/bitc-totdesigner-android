package ru.bitc.totdesigner.ui.loading

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.fragment_loading_detailed.*
import kotlinx.android.synthetic.main.item_loading_detailed.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailedState
import ru.bitc.totdesigner.ui.loading.state.LoadingMiniItem

/**
 * Created on 04.02.2020
 * @author YWeber */

class LoadingDetailedFragment : BaseFragment(R.layout.fragment_loading_detailed) {

    private val viewModel by viewModel<LoadingDetailedViewModel>()

    private val loadingAdapter by lazy {
        ListDelegationAdapter<List<LoadingMiniItem>>(loadingDetailedAdapter({}))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvLoading.adapter = loadingAdapter
        subscribe(viewModel.state, ::handleState)
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

    private fun loadingDetailedAdapter(cancelEvent: (LoadingMiniItem) -> Unit) =
            adapterDelegateLayoutContainer<LoadingMiniItem, LoadingMiniItem>(R.layout.item_loading_detailed) {
                tvCancelLoading.click {
                    cancelEvent(item)
                }
                bind {
                    ivLoading.loadImage(item.imageUrl)
                    val correctProgress = item.progress + pbLoading.progress
                    if (item.progress < 10) pbLoading.progress = 0
                    val animation = ObjectAnimator.ofInt(pbLoading, "progress", pbLoading.progress, correctProgress)
                    animation.duration = 800
                    animation.interpolator = LinearOutSlowInInterpolator()
                    animation.start()
                    tvTitle.text = item.title

                }
            }

    companion object {
        fun newInstance() = with(LoadingDetailedFragment()) {
            this
        }
    }
}