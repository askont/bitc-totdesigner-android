package ru.bitc.totdesigner.platfom.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_header_loading.*
import kotlinx.android.synthetic.main.item_loading_detailed.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.LoadingDetailed
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage
import kotlin.random.Random

/**
 * Created on 19.02.2020
 * @author YWeber */

class LoadingAdapterDelegate {

    private val interpolator = DecelerateInterpolator()

    fun createDelegate(click: (LoadingDetailed) -> Unit) =
        AsyncListDifferDelegationAdapter<LoadingDetailed>(
            DetailedDiff,
            AdapterDelegatesManager<List<LoadingDetailed>>()
                .addDelegate(loadingDetailedAdapter(click))
                .addDelegate(errorLoadingAdapter(click))
                .addDelegate(finishLoadingDetailedAdapter(click))
                .addDelegate(headerLoadingAdapter())
        )

    private fun headerLoadingAdapter() =
        adapterDelegateLayoutContainer<LoadingDetailed.HeaderTitle, LoadingDetailed>(R.layout.item_header_loading) {
            bind {
                tvHeader.text = item.title
            }
        }

    private fun loadingDetailedAdapter(cancelEvent: (LoadingDetailed) -> Unit) =
        adapterDelegateLayoutContainer<LoadingDetailed.Loading, LoadingDetailed>(R.layout.item_loading_detailed) {
            tvCancelLoading.click {
                cancelEvent(item)
            }
            bind {
                ivLoading.loadImage(item.imageUrl)
                val animation = ObjectAnimator.ofInt(pbLoading, "progress", 0, 1000)
                animation.duration = item.durationProgress + Random.nextLong(10000) * Random.nextDouble(5.0).toInt()
                animation.interpolator = interpolator
                animation.repeatCount = ObjectAnimator.INFINITE
                animation.repeatMode = ObjectAnimator.RESTART
                animation.start()
                tvTitle.text = item.title
            }
        }

    private fun finishLoadingDetailedAdapter(openEvent: (LoadingDetailed) -> Unit) =
        adapterDelegateLayoutContainer<LoadingDetailed.Finish, LoadingDetailed>(R.layout.item_loading_detailed) {
            tvCancelLoading.click {
                openEvent(item)
            }
            bind {
                ivLoading.loadImage(item.imageUrl)
                pbLoading.progress = 1000
                tvTitle.text = item.title
                tvCancelLoading.text = getString(R.string.open)
                tvHint.text = getString(R.string.done)
            }
        }


    private fun errorLoadingAdapter(retryEvent: (LoadingDetailed) -> Unit) =
        adapterDelegateLayoutContainer<LoadingDetailed.Error, LoadingDetailed>(R.layout.item_loading_detailed) {
            tvCancelLoading.click {
                retryEvent(item)
            }
            bind {
                ivLoading.loadImage(item.imageUrl)
                pbLoading.progress = 0
                tvTitle.text = item.title
                tvHint.text = getString(R.string.download_error_hint)
                tvCancelLoading.text = getString(R.string.retry)
            }
        }
}

private object DetailedDiff : DiffUtil.ItemCallback<LoadingDetailed>() {
    override fun areItemsTheSame(oldItem: LoadingDetailed, newItem: LoadingDetailed): Boolean =
        oldItem.urlId == newItem.urlId

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LoadingDetailed, newItem: LoadingDetailed): Boolean =
        oldItem == newItem
}
