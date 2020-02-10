package ru.bitc.totdesigner.ui.loading

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.system.printDebug
import timber.log.Timber

/**
 * Created on 04.02.2020
 * @author YWeber */

class LoadingDetailedFragment : BaseFragment(R.layout.fragment_loading_detailed) {

    private val viewModel by viewModel<LoadingDetailedViewModel>()

    /*private val loadingAdapter by lazy {
        ListDelegationAdapter<List<LoadingItem>>(loadingAdapter(viewModel::cancelLoading))
    }*/

    override fun onBackPressed(): Boolean {
        viewModel.backTo()
        return true
    }

   /* private fun loadingAdapter(cancelEvent: (LoadingItem) -> Unit) =
        adapterDelegateLayoutContainer<LoadingItem, LoadingItem>(R.layout.item_download) {
            tvCancelLoading.click {
                cancelEvent(item)
            }
            bind {
                val correctProgress = item.progress + pbLoading.progress
                if (item.progress < 10) pbLoading.progress = 0
                val animation = ObjectAnimator.ofInt(pbLoading, "progress", pbLoading.progress, correctProgress)
                animation.duration = 800
                animation.interpolator = LinearOutSlowInInterpolator()
                animation.start()
                tvProgressTitle.text = item.message

            }
        }*/

    companion object {
        fun newInstance() = with(LoadingDetailedFragment()) {
            this
        }
    }
}