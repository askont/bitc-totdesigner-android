package ru.bitc.totdesigner.ui.catalog.dialog

import android.graphics.Point
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_free_download.*
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseDialog

/**
 * Created on 22.01.2020
 * @author YWeber */

class FreeDownloadDialog : BaseDialog(R.layout.dialog_free_download) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTitle.text = "Основы проектирования UX UI - Wireframe"
        tvDescription.text =
            "Вы можете загрузить данный интерактивный 2D/3D/AR курс и/или образовательный игровой квест бесплатно, без подписки. Нажмите «Скачать курс» или продолжите выбор в каталоге"
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
}