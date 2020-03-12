package ru.bitc.totdesigner.ui.interaction

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_interaction.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.adapter.ineraction.InteractionPartDelegateAdapter
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.bitc.totdesigner.system.loadImage
import ru.bitc.totdesigner.system.printDebug
import ru.bitc.totdesigner.system.setData
import ru.bitc.totdesigner.system.subscribe
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.terrakok.cicerone.Navigator

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionActivity : BaseActivity(R.layout.activity_interaction) {

    override val viewModel: InteractionViewModel by viewModel()

    override val navigator: Navigator
        get() = ActivityNavigatorProxy(this, R.id.rootContainerInteractive)

    private val partAdapter by lazy {
        InteractionPartDelegateAdapter().createAdapter { }
    }

    private val previewAdapter by lazy {
        InteractionPartDelegateAdapter().createAdapter { }
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
        ivMainImage.loadImage(state.rootPicture)
        partAdapter.setData((state.partImages.printDebug()))
        previewAdapter.setData(state.previewImages)

    }
}