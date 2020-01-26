package ru.bitc.totdesigner.ui.catalog.dialog

import android.graphics.Point
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_free_download.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseDialog
import ru.bitc.totdesigner.system.click
import ru.bitc.totdesigner.system.loadImage
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.catalog.dialog.state.DownLoadViewState

/**
 * Created on 22.01.2020
 * @author YWeber */

class FreeDownloadDialog : BaseDialog(R.layout.dialog_free_download) {

    private val viewModel by viewModel<DownLoadViewModel> { parametersOf(arguments?.get(NAME_QUEST) ?: "") }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDialogSize()
        viewModel.initState()
        subscribe(viewModel.viewState, ::handleState)
        tvBack.click { dismiss() }
        tvBack.click { viewModel.download() }
    }

    private fun setDialogSize() {
        val defaultDisplay = requireActivity().windowManager.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        containerDialogContent.setPadding(
            (point.x * 0.2).toInt(),
            (point.x * 0.05).toInt(),
            (point.x * 0.2).toInt(),
            (point.x * 0.05).toInt()
        )
    }

    private fun handleState(downLoadViewState: DownLoadViewState) {
        when (downLoadViewState) {
            is DownLoadViewState.Free -> {
                tvDescription.text = downLoadViewState.description
                tvTitle.text = downLoadViewState.nameQuest
                tvDownload.text = getString(R.string.download)
                ivImageQuest.loadImage(downLoadViewState.url)
            }
            is DownLoadViewState.Paid -> {
                tvDescription.text = downLoadViewState.description
                tvTitle.text = downLoadViewState.nameQuest
                tvDownload.text = getString(R.string.issue_subscription)
                ivImageQuest.loadImage(downLoadViewState.url)
            }
        }
    }

    companion object {
        const val NAME_QUEST = "NAME_QUEST"
        fun newInstance(quest: String) = with(FreeDownloadDialog()) {
            val bundle = Bundle()
            bundle.putString(NAME_QUEST, quest)
            arguments = bundle
            this
        }
    }
}