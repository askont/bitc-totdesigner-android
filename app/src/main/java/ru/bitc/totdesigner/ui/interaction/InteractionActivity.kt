package ru.bitc.totdesigner.ui.interaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_interaction.*
import kotlinx.android.synthetic.main.toolbar_interaction.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.adapter.ineraction.InteractionPartDelegateAdapter
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.bitc.totdesigner.system.*
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
        ivListPart.click { viewModel.switchSide() }
        rvPartImage.adapter = partAdapter
        rvPartImage.addItemDecoration(TopBottomSpaceDecorator(4))
        rvPreviewInteraction.adapter = previewAdapter
        rvPreviewInteraction.addItemDecoration(GridPaddingItemDecoration(8))
        hintEmptyPartImage.movementMethod = ScrollingMovementMethod()
    }

    private fun renderState(state: InteractionState) {
        ivListPart.isVisible = state.sceneState.disableSwitch
        hintEmptyPartImage.isVisible = state.sceneState.visibleDescription
        hintEmptyPartImage.scrollTo(0, 0)
        rvPartImage.isVisible = !state.sceneState.visibleDescription
        hintEmptyPartImage.htmlText(state.sceneState.description)
        partAdapter.setData((state.sceneState.partImages))
        previewAdapter.setData(state.previewImages)

        rootSceneContainer.removeAllViews()
        state.sceneState.imageParticle.forEach {
            val offsetWidth = (1920F / rootSceneContainer.width.toFloat())
            val offsetHeight = (1080F / rootSceneContainer.height.toFloat())
            Toast.makeText(this, "width:${offsetWidth} height:${offsetHeight}", Toast.LENGTH_LONG)
                .show()
            val params = FrameLayout.LayoutParams((it.width / offsetWidth).toInt(), (it.height / offsetHeight).toInt())
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.loadFileImage(it.path)
            params.topMargin = (it.positionY / offsetHeight).toInt()
            params.marginStart = (it.positionX / offsetWidth).toInt()
            rootSceneContainer.addView(imageView, params)
        }
    }

    companion object {
        private const val START_LESSON_PATH = "start interaction path"
        fun newInstance(lessonPath: String, context: Context?) =
            Intent(context, InteractionActivity::class.java).apply {
                putExtra(START_LESSON_PATH, lessonPath)
            }
    }
}