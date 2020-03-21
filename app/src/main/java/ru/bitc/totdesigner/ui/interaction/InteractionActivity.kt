package ru.bitc.totdesigner.ui.interaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.transition.Slide
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_interaction.*
import kotlinx.android.synthetic.main.toolbar_interaction.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.bitc.totdesigner.R
import ru.bitc.totdesigner.platfom.BaseActivity
import ru.bitc.totdesigner.platfom.adapter.ineraction.InteractionPartDelegateAdapter
import ru.bitc.totdesigner.platfom.decorator.GridPaddingItemDecoration
import ru.bitc.totdesigner.platfom.decorator.TopBottomSpaceDecorator
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.bitc.totdesigner.system.*
import ru.bitc.totdesigner.system.notifier.WindowsSizeNotifier
import ru.bitc.totdesigner.ui.interaction.state.ImageParticle
import ru.bitc.totdesigner.ui.interaction.state.InteractionState
import ru.bitc.totdesigner.ui.interaction.state.WindowsSize
import ru.terrakok.cicerone.Navigator

/**
 * Created on 10.03.2020
 * @author YWeber */

class InteractionActivity : BaseActivity(R.layout.activity_interaction) {

    override val viewModel: InteractionViewModel by viewModel {
        parametersOf(intent.getStringExtra(START_LESSON_PATH) ?: "")
    }

    private val windowSizeNotifier: WindowsSizeNotifier by inject()

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
        rootSceneContainer.viewTreeObserver.addOnGlobalLayoutListener {
            windowSizeNotifier.setNewWindowSize(WindowsSize(rootSceneContainer.height, rootSceneContainer.width))
        }
        tvBackToCatalog.click { viewModel.back() }
        tvNextStage.click { viewModel.nextScene() }
        ivPlay.click { viewModel.playOrStopInteractive() }
    }

    private fun renderState(state: InteractionState) {
        renderRunPlay(state)
        renderContentInteractive(state)
        partAdapter.setData((state.sceneState.partImages))
        previewAdapter.setData(state.previewImages)
        renderInteractive(state)
    }

    private fun renderRunPlay(state: InteractionState) {
        if (state.sceneState.isRunPlay) {
            ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_interactive))
            ivPlay.background = ContextCompat.getDrawable(this, R.drawable.bg_interactive)
        } else {
            ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_run_play))
            ivPlay.background = null
        }
        ivPlay.isVisible = state.sceneState.isContentInteractive
    }

    private fun renderContentInteractive(state: InteractionState) {
        ivListPart.isVisible = state.sceneState.isContentInteractive
        hintEmptyPartImage.isVisible = state.sceneState.visibleDescription
        hintEmptyPartImage.scrollTo(0, 0)
        rvPartImage.isVisible = !state.sceneState.visibleDescription
        hintEmptyPartImage.htmlText(state.sceneState.description)
    }

    private fun renderInteractive(state: InteractionState) {
        rootSceneContainer.removeAllViews()
        state.sceneState.imageParticle.asSequence().forEach {
            val params = FrameLayout.LayoutParams(it.width, it.height)
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.loadFileImage(it.path)
            params.topMargin = it.positionY
            params.marginStart = it.positionX
            animationScene(it, state)
            rootSceneContainer.addView(imageView, params)
        }
    }

    private fun animationScene(
        particle: ImageParticle,
        state: InteractionState
    ) {
        if (!particle.isStatic) {
            val slide = Slide(Gravity.START)
            slide.duration = 1000
            TransitionManager.beginDelayedTransition(rootSceneContainer, slide)
        } else if (particle.isStatic && !state.sceneState.isContentInteractive) {
            val slide = Slide(Gravity.TOP)
            slide.duration = 600
            TransitionManager.beginDelayedTransition(rootSceneContainer, slide)
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