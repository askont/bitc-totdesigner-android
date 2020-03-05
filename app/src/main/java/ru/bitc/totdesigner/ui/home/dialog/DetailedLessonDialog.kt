package ru.bitc.totdesigner.ui.home.dialog

import android.os.Bundle
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_detailed_lesson.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseDialog

/**
 * Created on 05.03.2020
 * @author YWeber */

class DetailedLessonDialog : BaseDialog(R.layout.dialog_detailed_lesson) {
    override val rootContainer: ViewGroup
        get() = rootContainerDetailedLesson

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDialogSize(0.40F, 0.010F)
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