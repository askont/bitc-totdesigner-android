package ru.bitc.totdesigner.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.HomeLessonDelegateAdapter
import ru.bitc.totdesigner.platfom.adapter.state.BottomHomeLessonItem
import ru.bitc.totdesigner.platfom.adapter.state.HeaderHomeLesson
import ru.bitc.totdesigner.platfom.adapter.state.TitleHomeLessonItem
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.system.dpToPx
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.home.state.HomeState

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel by viewModel<HomeViewModel>()

    private val adapter by lazy {
        HomeLessonDelegateAdapter().createAdapter(viewModel::eventClick)
    }

    private val decorator = GridPaddingItemDecoration(12)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun setupAdapter() {
        val gridManager = GridLayoutManager(context, 3)
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (
                    adapter.items[position] is TitleHomeLessonItem ||
                    adapter.items[position] is HeaderHomeLesson ||
                    adapter.items[position] is BottomHomeLessonItem
                ) 3 else 1
            }

        }
        rvCardHomeLesson.layoutManager = gridManager
        rvCardHomeLesson.addItemDecoration(decorator)
        rvCardHomeLesson.adapter = adapter
    }

    private fun handleState(homeState: HomeState) {
        adapter.setData(homeState.lessonItems)
        if (homeState.scrollToStart) {
            rvCardHomeLesson.smoothScrollToPosition(0)
        }
    }

    companion object {
        fun newInstance() = with(HomeFragment()) {
            this
        }
    }
}