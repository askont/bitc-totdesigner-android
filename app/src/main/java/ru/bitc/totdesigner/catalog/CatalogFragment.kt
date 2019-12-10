package ru.bitc.totdesigner.catalog

import android.os.Bundle
import android.view.View
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.catalog.state.CatalogState
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.adapter.QuestAdapterDelegate
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
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
        ListDelegationAdapter(QuestAdapterDelegate().createDelegate())
    }

    private val decorator = GridPaddingItemDecoration(3, 24.dpToPx(),false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateState()
        rvCardQuest.addItemDecoration(decorator)
        rvCardQuest.adapter = adapter
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun handleState(catalogState: CatalogState) {
        adapter.setData(catalogState.questItems)
        tvDescription.text = catalogState.description
        tvTitle.text = catalogState.title
    }

    companion object {
        fun newInstance() = with(CatalogFragment()) {
            this
        }
    }
}