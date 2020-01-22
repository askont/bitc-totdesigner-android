package ru.bitc.totdesigner.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.ui.catalog.state.CatalogState
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.QuestAdapterDelegate
import ru.bitc.totdesigner.platfom.adapter.state.ButtonQuestItem
import ru.bitc.totdesigner.platfom.adapter.state.QuestItem
import ru.bitc.totdesigner.platfom.adapter.state.TitleQuestItem
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.platfom.state.State
import ru.bitc.totdesigner.system.dpToPx
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateState()
        setupManager()
        rvCardQuest.addItemDecoration(decorator)
        containerContent.setOutlineProvider(null)
        rvCardQuest.adapter = adapter
        inputTextSearch.addTextChangedListener {
            viewModel.search(it?.toString() ?: "")
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
                return if (adapter.items[position] is TitleQuestItem || adapter.items[position] is ButtonQuestItem) 3 else 1
            }
        }
    }

    private fun handleClick(questItem: QuestItem) {
        viewModel.eventClick(questItem)
    }

    private fun handleState(catalogState: CatalogState) {
        adapter.setData(catalogState.questItems)
        handleSearch(catalogState)
        tvDescription.text = catalogState.description
        tvTitle.text = catalogState.title
        handleLoading(catalogState)
        if (catalogState.scrollToStart) {
            rvCardQuest.scrollToPosition(0)
            containerContent.setExpanded(true)
        }
    }

    private fun handleSearch(catalogState: CatalogState) {
        rvCardQuest.isVisible = catalogState.questItemEmpty
        tvEmptySearch.isVisible = !catalogState.questItemEmpty
        if (catalogState.questItemEmpty) {
            tvEmptySearch.text = getString(R.string.search_empty_catalog, catalogState.lastSearchQuest)
        }
    }

    private fun handleLoading(catalogState: CatalogState) {
        containerContent.isVisible = catalogState.state is State.Loaded
        layoutSearch.isVisible = catalogState.state is State.Loaded
        pbLoading.isVisible = catalogState.state is State.Loading
    }

    companion object {
        fun newInstance() = with(CatalogFragment()) {
            this
        }
    }
}