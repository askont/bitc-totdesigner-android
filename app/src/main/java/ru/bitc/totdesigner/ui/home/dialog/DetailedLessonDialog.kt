package ru.bitc.totdesigner.ui.home.dialog

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_detailed_lesson.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseDialog
import ru.bitc.totdesigner.platfom.adapter.DetailedLessonDelegateAdapter
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.home.dialog.state.DetailedLessonState

/**
 * Created on 05.03.2020
 * @author YWeber */

class DetailedLessonDialog : BaseDialog(R.layout.dialog_detailed_lesson) {

    override val rootContainer: ViewGroup
        get() = rootContainerDetailedLesson

    private val viewModel by viewModel<DetailedLessonViewModel> {
        parametersOf(arguments?.getString(REMOTE_PATH_EXTRA) ?: "")
    }

    private val adapter by lazy {
        DetailedLessonDelegateAdapter().createAdapter(viewModel::selectNewPreview)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDialogSize(0.40F, 0.010F)
        setupView()
        subscribe(viewModel.viewState, ::handleState)
    }

    private fun setupView() {
        rvLessonDetailed.adapter = adapter
        rvLessonDetailed.addItemDecoration(TopBottomSpaceDecorator(4))
        rvLessonDetailed.layoutManager = LinearLayoutManager(context)
        tvBackCatalog.click { viewModel.back() }
        tvDelete.click { viewModel.deleteLesson() }
        tvRunLesson.click { viewModel.runInteractive() }
        rootContainerDetailedLesson.click { viewModel.back() }

    }

    private fun handleState(state: DetailedLessonState) {
        adapter.setData(state.detailedItems)
        if (state.exit) {
            dismiss()
        }
    }


    companion object {
        private const val REMOTE_PATH_EXTRA = "remote path detailed lesson"
        fun newInstance(remotePath: String) = with(DetailedLessonDialog()) {
            val bundle = Bundle()
            bundle.putString(REMOTE_PATH_EXTRA, remotePath)
            arguments = bundle
            this
        }
    }
}