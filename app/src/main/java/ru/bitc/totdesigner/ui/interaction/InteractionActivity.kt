package ru.bitc.totdesigner.ui.interaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_interaction.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.adapter.ineraction.InteractionPartDelegateAdapter
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.bitc.totdesigner.system.loadFileImage
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.terrakok.cicerone.Navigator

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionActivity : BaseActivity(R.layout.activity_interaction) {

    override val viewModel: InteractionViewModel by viewModel {
        parametersOf(intent.getStringExtra(START_LESSON_PATH) ?: "")
    }

    override val navigator: Navigator
        get() = ActivityNavigatorProxy(this, R.id.rootContainerInteractive)

    private val partAdapter by lazy {
        InteractionPartDelegateAdapter().createAdapter(viewModel::selectImage)
    }

    private val previewAdapter by lazy {
        InteractionPartDelegateAdapter().createAdapter(viewModel::selectImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        subscribe(viewModel.viewState, ::renderState)
    }

    private fun setupView() {
        rvPartImage.adapter = partAdapter
        rvPartImage.addItemDecoration(TopBottomSpaceDecorator(4))
        rvPreviewInteraction.adapter = previewAdapter
        rvPreviewInteraction.addItemDecoration(GridPaddingItemDecoration(8))
    }

    private fun renderState(state: InteractionState) {
        ivMainImage.loadFileImage(state.rootPicture)
        partAdapter.setData((state.partImages))
        previewAdapter.setData(state.previewImages)

    }

    companion object {
        private const val START_LESSON_PATH = "start interaction path"
        fun newInstance(lessonPath: String, context: Context?) =
            Intent(context, InteractionActivity::class.java).apply {
                putExtra(START_LESSON_PATH, lessonPath)
            }
    }
}