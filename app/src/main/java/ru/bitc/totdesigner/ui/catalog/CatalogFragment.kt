package ru.bitc.totdesigner.ui.catalog

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.QuestAdapterDelegate
import ru.bitc.totdesigner.platfom.adapter.state.ButtonQuestItem
import ru.bitc.totdesigner.platfom.adapter.state.HeaderItem
import ru.bitc.totdesigner.platfom.adapter.state.QuestItem
import ru.bitc.totdesigner.platfom.adapter.state.TitleQuestItem
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
        ListDelegationAdapter(QuestAdapterDelegate().createDelegate(::handleClick))
    }

    private val decorator = GridPaddingItemDecoration(12.dpToPx())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupManager()
        rvCardQuest.addItemDecoration(decorator)
        rvCardQuest.adapter = adapter
        inputTextSearch.querySearch {
            if (it == null) return@querySearch
            viewModel.search(it.toString())
        }
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun setupManager() {
        val gridManager = GridLayoutManager(context, 3)
        changerColumn(gridManager)
        rvCardQuest.layoutManager = gridManager
    }

    private fun changerColumn(gridManager: GridLayoutManager) {
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.items[position] is TitleQuestItem || adapter.items[position] is ButtonQuestItem || adapter.items[position] is HeaderItem) 3 else 1
            }
        }
    }

    private fun handleClick(questItem: QuestItem) {
        viewModel.eventClick(questItem)
    }

    private fun handleState(catalogState: CatalogState) {
        adapter.setData(catalogState.questItems)
        handleSearch(catalogState)
        handleLoading(catalogState)
        if (catalogState.scrollToStart) {
            appBarLayoutSearch.setExpanded(true, true)
            rvCardQuest.smoothScrollToPosition(0)
        }
    }

    private fun handleSearch(catalogState: CatalogState) {
        tvEmptySearch.isVisible = !catalogState.questItemEmpty
        rvCardQuest.isVisible = catalogState.questItemEmpty
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