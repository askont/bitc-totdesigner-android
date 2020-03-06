package ru.bitc.totdesigner.ui.catalog

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.LessonAdapterDelegate
import ru.bitc.totdesigner.platfom.adapter.state.ButtonLessonItem
import ru.bitc.totdesigner.platfom.adapter.state.HeaderItem
import ru.bitc.totdesigner.platfom.adapter.state.LessonItem
import ru.bitc.totdesigner.platfom.adapter.state.TitleLessonItem
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.dpToPx
import ru.bitc.totdesigner.system.querySearch
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.catalog.state.CatalogState

/*
 * Created on 2019-12-06
 * @author YWeber
 */
class CatalogFragment : BaseFragment(R.layout.fragment_catalog) {

    private val viewModel by viewModel<CatalogViewModel>()

    private val adapter by lazy {
        LessonAdapterDelegate().createAdapter(::handleClick)
    }

    private val decorator = GridPaddingItemDecoration(12)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupManager()
        rvCardHomeLesson.addItemDecoration(decorator)
        rvCardHomeLesson.adapter = adapter
        inputTextSearch.querySearch {
            if (it == null) return@querySearch
            viewModel.search(it.toString())
        }
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun setupManager() {
        val gridManager = GridLayoutManager(context, 3)
        changerColumn(gridManager)
        rvCardHomeLesson.layoutManager = gridManager
    }

    private fun changerColumn(gridManager: GridLayoutManager) {
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.items[position] is TitleLessonItem || adapter.items[position] is ButtonLessonItem || adapter.items[position] is HeaderItem) 3 else 1
            }
        }
    }

    private fun handleClick(lessonItem: LessonItem) {
        viewModel.eventClick(lessonItem)
    }

    private fun handleState(catalogState: CatalogState) {
        adapter.setData(catalogState.lessonItems)
        handleSearch(catalogState)
        handleLoading(catalogState)
        if (catalogState.scrollToStart) {
            appBarLayoutSearch.setExpanded(true, true)
            rvCardHomeLesson.smoothScrollToPosition(0)
        }
    }
    private fun handleSearch(catalogState: CatalogState) {
        tvEmptySearch.isVisible = !catalogState.questItemEmpty
        rvCardHomeLesson.isVisible = catalogState.questItemEmpty
        tvEmptySearch.text = getString(R.string.search_empty_catalog, catalogState.lastSearchQuest)
    }

    private fun handleLoading(catalogState: CatalogState) {
        pbLoading.isVisible = catalogState.state is State.Loading
    }

    companion object {
        fun newInstance() = with(CatalogFragment()) {
            this
        }
    }
}