package ru.bitc.totdesigner.ui.home

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.QuestAdapterDelegate
import ru.bitc.totdesigner.platfom.adapter.state.LessonItem
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.system.dpToPx
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.home.state.HomeState

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel by viewModel<HomeViewModel>()

    private val adapter by lazy {
        QuestAdapterDelegate().createDelegate(::click)
    }

    private val decorator = GridPaddingItemDecoration(12.dpToPx())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCardQuest.addItemDecoration(decorator)
        rvCardQuest.adapter = adapter
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun handleState(homeState: HomeState) {
        adapter.setData(homeState.lessonItems)
        tvDescription.text = homeState.description
        tvTitle.text = homeState.title
    }

    private fun click(lessonItem: LessonItem) {

    }

    companion object {
        fun newInstance() = with(HomeFragment()) {
            this
        }
    }
}