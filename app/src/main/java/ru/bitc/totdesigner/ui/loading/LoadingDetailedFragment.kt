package ru.bitc.totdesigner.ui.loading

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.fragment_loading_detailed.*
import kotlinx.android.synthetic.main.item_loading_detailed.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailed
import ru.bitc.totdesigner.ui.loading.state.LoadingDetailedState

/**
 * Created on 04.02.2020
 * @author YWeber */

class LoadingDetailedFragment : BaseFragment(R.layout.fragment_loading_detailed) {

    private val viewModel by viewModel<LoadingDetailedViewModel>()

    private val loadingAdapter by lazy {
        AsyncListDifferDelegationAdapter<LoadingDetailed>(
            LoadingDetailed.DetailedDiff,
            AdapterDelegatesManager(loadingDetailedAdapter {})
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvLoading.adapter = loadingAdapter
        rvLoading.addItemDecoration(TopBottomSpaceDecorator())
        subscribe(viewModel.state, ::handleState)
    }

    private fun handleState(loadingDetailedState: LoadingDetailedState) {
        loadingAdapter.items = loadingDetailedState.loadingMiniItems
    }

    override fun onBackPressed(): Boolean {
        viewModel.backTo()
        return true
    }

    override fun onDestroyView() {
        viewModel.onVisible()
        super.onDestroyView()
    }

    private fun loadingDetailedAdapter(cancelEvent: (LoadingDetailed) -> Unit) =
        adapterDelegateLayoutContainer<LoadingDetailed.Loading, LoadingDetailed>(R.layout.item_loading_detailed) {
            tvCancelLoading.click {
                cancelEvent(item)
            }
            bind {
                ivLoading.loadImage(item.imageUrl)
                val animation = ObjectAnimator.ofInt(pbLoading, "progress", 0, 1000)
                animation.duration = item.durationProgress
                animation.interpolator = DecelerateInterpolator()
                animation.repeatCount = ObjectAnimator.INFINITE
                animation.repeatMode = ObjectAnimator.RESTART
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