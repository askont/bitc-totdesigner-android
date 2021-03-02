package ru.bitc.totdesigner.ui.interaction

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
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
import ru.bitc.totdesigner.platfom.drag.ScaleDragShadowBuilder
import ru.bitc.totdesigner.platfom.drag.dragView
import ru.bitc.totdesigner.platfom.navigation.ActivityNavigatorProxy
import ru.bitc.totdesigner.platfom.navigation.AppScreens
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
        get() = ActivityNavigatorProxy(this, R.id.rootInteractiveContainer)

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
        tvHintEmptyPartImage.movementMethod = ScrollingMovementMethod()
        rootSceneContainer.viewTreeObserver.addOnGlobalLayoutListener {
            windowSizeNotifier.setNewWindowSize(
                WindowsSize(
                    rootZoomContainer.height,
                    rootZoomContainer.width
                )
            )
        }
        ivCube.setOnClickListener {
           viewModel.navigateStatistic()
        }
        tvBackToCatalog.click { viewModel.back() }
        tvNextStage.click { viewModel.nextScene() }
        ivPlay.click { viewModel.playOrStopInteractive() }
        rootSceneContainer.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val clipPathId = event.clipData.getItemAt(0)
                    if (clipPathId != null) {
                        viewModel.handleDragParticle(
                            clipPathId.text.toString(),
                            event.x.toInt(),
                            event.y.toInt()
                        )
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    partAdapter.notifyDataSetChanged()
                }
            }
            true
        }
        ivDeleteParticle.click {
            viewModel.deleteAllMarkParticle()
        }
    }

    private fun renderState(state: InteractionState) {
        renderRunPlay(state)
        renderContentInteractive(state)
        partAdapter.setData((state.sceneState.partImages))
        previewAdapter.setData(state.previewImages)
        renderInteractive(state)
        if (state.sceneState.imageParticle.any { it.isDeleteCandidate }) {
            ivDeleteParticle.isEnabled = true
            ivDeleteParticle.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_delete_full_interactive
                )
            )
        } else {
            ivDeleteParticle.isEnabled = false
            ivDeleteParticle.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_delete_empty_interactive
                )
            )
        }
    }

    private fun renderRunPlay(state: InteractionState) {
        tvHintDoneLesson.isVisible =
            state.sceneState.isDoneInteractive && state.sceneState.isRunPlay && !state.sceneState.visibleDescription

        if (state.sceneState.isRunPlay) {
            ivPlay.background = ContextCompat.getDrawable(this, R.drawable.bg_interactive)
            if (state.sceneState.isDoneInteractive) {
                ivPlay.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_done_run_button
                    )
                )
            } else {
                ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_stop))

            }
        } else {
            ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_run_play))
            ivPlay.background = null
        }
        ivPlay.isVisible = state.sceneState.isContentInteractive
        ivDeleteParticle.isVisible = state.sceneState.isContentInteractive
    }

    private fun renderContentInteractive(state: InteractionState) {
        ivListPart.isVisible = state.sceneState.isContentInteractive
        tvHintEmptyPartImage.isVisible = state.sceneState.visibleDescription
        tvHintEmptyPartImage.scrollTo(0, 0)
        rvPartImage.isVisible =
            !state.sceneState.visibleDescription && !state.sceneState.isDoneInteractive
        tvHintEmptyPartImage.htmlText(state.sceneState.description)
    }

    private fun renderInteractive(state: InteractionState) {
        if (!state.sceneState.changeParticle) return
        rootSceneContainer.removeAllViews()
        state.sceneState.imageParticle.asSequence().forEach { particle ->
            val params = FrameLayout.LayoutParams(particle.width, particle.height)
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.loadFileImage(particle.path)
            if (!particle.isSuccessArea) {
                if (particle.isDeleteCandidate) {
                    imageView.background =
                        ContextCompat.getDrawable(this, R.drawable.bg_contur_delete_particle)
                    imageView.setPadding(4.dpToPx())
                } else {
                    imageView.background = ContextCompat.getDrawable(this, R.drawable.bg_drag_error)
                    imageView.setPadding(4.dpToPx())
                }
            } else if (particle.isSuccessArea && particle.isAddAnimate) {
                val colorSuccess = ContextCompat.getColor(this, R.color.colorSuccess)
                val colorTransparent = ContextCompat.getColor(this, R.color.transparentSuccess)
                imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSuccess))
                ObjectAnimator.ofObject(ArgbEvaluator(), colorSuccess, colorTransparent).apply {
                    duration = 1000
                    addUpdateListener {
                        imageView.setBackgroundColor(it.animatedValue as Int)
                    }
                }.start()
            }
            params.topMargin = particle.positionY
            params.marginStart = particle.positionX
            animationScene(particle, state)
            if (!particle.isMoveAnimate) {
                imageView.click {
                    viewModel.markDeleteParticle(particle)
                }
                imageView.setOnLongClickListener {
                    val dragDate =
                        ScaleDragShadowBuilder.createDate(
                            particle.id,
                            particle.positionX.toString(),
                            particle.positionY.toString()
                        )
                    val dragImg = View.DragShadowBuilder(imageView)
                    it.dragView(dragDate, dragImg)
                    true
                }
            }
            rootSceneContainer.addView(imageView, params)
        }
    }


    private fun animationScene(
        particle: ImageParticle,
        state: InteractionState
    ) {
        if (!particle.isStatic && particle.isMoveAnimate) {
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