package ru.bitc.totdesigner.ui.main

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.navigation.SupportDialogAppNavigator
import ru.terrakok.cicerone.Navigator

/*
 * Created on 2019-12-01
 * @author YWeber
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel by viewModel<MainViewModel>()

    private val navigator: Navigator
        get() = SupportDialogAppNavigator(requireActivity(),childFragmentManager, R.id.mainContainer)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        viewModel.defaultSelect()
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

    companion object {
        fun newInstance() = with(MainFragment()) {
            this
        }
    }
}