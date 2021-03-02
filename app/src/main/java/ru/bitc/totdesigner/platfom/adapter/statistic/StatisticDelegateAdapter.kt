package ru.bitc.totdesigner.platfom.adapter.statistic

import androidx.core.content.ContextCompat
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_statistic.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.adapter.state.StatisticItem

object StatisticDelegateAdapter {

    fun createAdapter() = ListDelegationAdapter(createStatisticAdapter())

    private fun createStatisticAdapter() =
        adapterDelegateLayoutContainer<StatisticItem, StatisticItem>(
            R.layout.item_statistic
        ) {
            bind {
                val statusRes =
                    if (item.isDone) R.drawable.ic_statistic_done else R.drawable.ic_statistic_failed
                ivStatusStatistic.setImageDrawable(ContextCompat.getDrawable(context, statusRes))
                tvTitleLesson.text = item.titleLesson
                tvSubtitleStep.text = item.subtitleStep
                tvPercentStep.text = item.percent
                tvStepCountInMax.text = item.countStepInMaxStep
            }
        }

}