package ru.bitc.totdesigner.ui.statistic

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_statistic_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActionFragment
import ru.bitc.totdesigner.platfom.adapter.statistic.StatisticDelegateAdapter
import ru.bitc.totdesigner.system.loadImage
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.ui.statistic.state.StatisticState

class StatisticMainFragment :
    BaseActionFragment<StatisticState, StatisticMainVIewModel>(R.layout.fragment_statistic_main) {

    companion object {
        fun newInstance() = StatisticMainFragment()
    }

    override val viewModel: StatisticMainVIewModel by viewModel()
    private val adapter by lazy { StatisticDelegateAdapter.createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvStatisticTask.adapter = adapter

    }

    override fun renderState(state: StatisticState) {
        adapter.setData(state.itemStatistic)
        ivImageLesson.loadImage(state.imagePath)
        tvNameLesson.text = state.titleLesson
        tvDoneStageLessonCount.text = state.stepDone.toString()
        tvFailedStageLessonCount.text = state.stepFailed.toString()
        tvSkipStageLessonCount.text = state.stepSkip.toString()

    }

    override fun onDestroyView() {
        rvStatisticTask.adapter = null
        super.onDestroyView()
    }
}