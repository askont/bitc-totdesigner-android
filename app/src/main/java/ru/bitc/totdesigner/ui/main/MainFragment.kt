package ru.bitc.totdesigner.ui.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseFragment
import ru.bitc.totdesigner.platfom.navigation.SupportDialogAppNavigator
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.printDebug
import ru.bitc.totdesigner.system.subscribe
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

    private val interpolator = LinearOutSlowInInterpolator()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectHomeScreen()
        subscribe(viewModel.viewState, ::handleState)
        setupView()
    }

    private fun handleState(state: MainState) {

        containerLoading.isVisible = state.visibleLoadingHolder()
        val animator = ObjectAnimator.ofInt(pbLoading, "progress", 1, 1000)
        if (state.visibleLoadingHolder()) {
            animator.duration = state.durationProgress.toLong()
            animator.interpolator = interpolator
            animator.repeatMode = ObjectAnimator.RESTART
            animator.repeatCount = ObjectAnimator.INFINITE
            animator.start()
        } else {
            animator.cancel()
        }
        pbLoading.progress = state.durationProgress
        tvProgressHint.text = state.messageLoading
        if (state.isError) {
            Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG).show()
        }
    }


    private fun setupView() {
        tvCancelLoading.click { viewModel.cancelAllJobLoading() }
        containerLoading.click { viewModel.navigateToLoadingDetails() }
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

    override fun onBackPressed(): Boolean {
        "onBackPressed".printDebug()
        return childFragmentManager.fragments.lastOrNull {
            (it as? BaseFragment)?.onBackPressed() ?: false
        } is BaseFragment
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