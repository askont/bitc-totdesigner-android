package ru.bitc.totdesigner.main

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.catalog.CatalogFragment
import ru.bitc.totdesigner.home.HomeFragment
import ru.bitc.totdesigner.mock.MockFragment
import ru.bitc.totdesigner.platfom.BaseFragment

/*
 * Created on 2019-12-01
 * @author YWeber
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel by viewModel<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

    }

    private fun setupView() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    createFragment(HomeFragment.newInstance())
                    true
                }
                R.id.catalogItem -> {
                    createFragment(CatalogFragment.newInstance())
                    true
                }
                R.id.openFolderItem -> {
                    createFragment(MockFragment.newInstance("open folder item"))
                    true
                }
                R.id.settingItem -> {
                    createFragment(MockFragment.newInstance("Setting"))
                    true
                }
                else -> false
            }
        }
        bottomNavigation.selectedItemId = R.id.homeItem
    }

    private fun createFragment(fragment: BaseFragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .commit()
    }

    companion object {
        fun newInstance() = with(MainFragment()) {
            this
        }
    }
}