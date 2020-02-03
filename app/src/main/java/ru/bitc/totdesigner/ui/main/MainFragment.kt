package ru.bitc.totdesigner.ui.main

import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_download.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.navigation.SupportDialogAppNavigator
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.dpToPx
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.main.state.LoadingItem
import ru.bitc.totdesigner.ui.main.state.MainState
import ru.terrakok.cicerone.Navigator

/*
 * Created on 2019-12-01
 * @author YWeber
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel by viewModel<MainViewModel>()

    private val navigator: Navigator
        get() = SupportDialogAppNavigator(requireActivity(), childFragmentManager, R.id.mainContainer)

    private val loadingAdapter by lazy {
        ListDelegationAdapter<List<LoadingItem>>(loadingAdapter(viewModel::cancelLoading))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectHomeScreen()
        subscribe(viewModel.viewState, ::handleState)
        setupView()
    }

    private fun handleState(state: MainState) {
        loadingAdapter.setData(state.downloadsItem)
        rvDownloadHolder.isVisible = state.visibleDownload
        if (state.downloadsItem.size <= MAX_LINES_LOADING) {
            changeSizeDownloadHolder(FrameLayout.LayoutParams.WRAP_CONTENT)
        } else {
            changeSizeDownloadHolder(MAX_HEIGHT_LOADING.dpToPx())
        }
    }

    private fun changeSizeDownloadHolder(height: Int) {
        val layoutParams = rvDownloadHolder.layoutParams as FrameLayout.LayoutParams
        layoutParams.height = height
        rvDownloadHolder.layoutParams = layoutParams
    }

    private fun setupView() {
        rvDownloadHolder.adapter = loadingAdapter
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> viewModel.selectHomeScreen()
                R.id.catalogItem -> viewModel.selectCatalogScreen()
                R.id.openFolderItem -> viewModel.selectOpenDisk()
                R.id.settingItem -> viewModel.selectSetting()
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.addNavigator(navigator)
    }

    override fun onPause() {
        viewModel.deleteNavigator()
        super.onPause()
    }


    private fun loadingAdapter(cancelEvent: (LoadingItem) -> Unit) =
        adapterDelegateLayoutContainer<LoadingItem, LoadingItem>(R.layout.item_download) {
            tvCancelLoading.click {
                cancelEvent(item)
            }
            bind {
                pbLoading.progress = item.progress
                tvProgressTitle.text = item.message

            }
        }

    companion object {
        private const val MAX_LINES_LOADING = 2
        private const val MAX_HEIGHT_LOADING = 72
        fun newInstance() = with(MainFragment()) {
            this
        }
    }
}