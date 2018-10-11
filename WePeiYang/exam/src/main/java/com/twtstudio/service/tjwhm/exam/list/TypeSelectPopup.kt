package com.twtstudio.service.tjwhm.exam.list

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.blur.BlurPopupWindow
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.problem.ProblemActivity
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 7:54 PM, 2018/10/8.
 * Happy coding!
 */

class TypeSelectPopup(mContext: Context, private val startXY: Pair<Float, Float>, private val lessonID: Int) : BlurPopupWindow(mContext), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    lateinit var view: View

    private lateinit var tvSingleNum: TextView
    private lateinit var tvSingleEnter: TextView
    private lateinit var tvMultiNum: TextView
    private lateinit var tvMultiEnter: TextView
    private lateinit var tvTfNum: TextView
    private lateinit var tvTfEnter: TextView

    val density = context.resources.displayMetrics.density

    override fun createContentView(parent: ViewGroup): View = parent.context.layoutInflater
            .inflate(R.layout.exam_popup_type_select, parent, false).apply {
                layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER
                    horizontalMargin = dip(30)
                }
            }.also {
                view = it
                tvSingleNum = view.findViewById(R.id.tv_type_select_single_num)
                tvSingleEnter = view.findViewById(R.id.tv_type_select_single_enter)
                tvMultiNum = view.findViewById(R.id.tv_type_select_multi_num)
                tvMultiEnter = view.findViewById(R.id.tv_type_select_multi_enter)
                tvTfNum = view.findViewById(R.id.tv_type_select_tf_num)
                tvTfEnter = view.findViewById(R.id.tv_type_select_tf_enter)
            }

    @SuppressLint("SetTextI18n")
    override fun onShow() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)

        getLessonInfo(lessonID.toString()) { it ->
            when (it) {
                is RefreshState.Failure -> Toasty.error(context, "网络错误").show()
                is RefreshState.Success -> {
                    it.message.data!!.apply {
                        tvSingleNum.text = "$single_done_count/$single_num"
                        tvMultiNum.text = "$multi_done_count/$multi_num"
                        tvTfNum.text = "$decide_done_count/$decide_num"
                        val intent = Intent(context, ProblemActivity::class.java).apply {
                            putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                            putExtra(ProblemActivity.LESSON_ID_KEY, lessonID)
                        }
                        when {
                            single_num.toInt() == 0 -> {
                                tvSingleNum.text = "无"
                                tvSingleEnter.visibility = View.GONE
                            }
                            single_done_count == 0 -> {
                                tvSingleEnter.apply {
                                    text = "开始练习"
                                    setOnClickListener {
                                        intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.SINGLE_CHOICE)
                                        context.startActivity(intent)
                                    }
                                }
                            }
                            else -> tvSingleEnter.text = "继续练习"
                        }
                        when {
                            multi_num.toInt() == 0 -> {
                                tvMultiNum.text = "无"
                                tvMultiEnter.visibility = View.GONE
                            }
                            multi_done_count == 0 -> {
                                tvMultiEnter.apply {
                                    text = "开始练习"
                                    setOnClickListener {
                                        intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.MULTI_CHOICE)
                                        context.startActivity(intent)
                                    }
                                }

                            }
                            else -> tvMultiEnter.text = "继续练习"
                        }
                        when {
                            decide_num.toInt() == 0 -> {
                                tvTfNum.text = "无"
                                tvTfEnter.visibility = View.GONE
                            }
                            decide_done_count == 0 -> {
                                tvTfEnter.apply {
                                    text = "开始练习"
                                    setOnClickListener {
                                        intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.TRUE_FALSE)
                                        context.startActivity(intent)
                                    }
                                }

                            }
                            else -> tvTfEnter.text = "继续练习"
                        }
                    }
                }
            }
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun createShowAnimator(): Animator {
        val alphaAnim = super.createShowAnimator()
        val animSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        val x = ObjectAnimator.ofFloat(view, "X", startXY.first, 30f * density)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(alphaAnim).with(x)
        return animSet
    }

    override fun createDismissAnimator(): Animator {
        val animSet = AnimatorSet()
        val contentAnim = ObjectAnimator.ofFloat(mContentLayout, "alpha", mContentLayout.alpha, 0f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        val x = ObjectAnimator.ofFloat(view, "X", 30f * density, startXY.first)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(contentAnim).with(x)
        return animSet
    }

    override fun getAnimationDuration(): Long = 200L

    override fun isDismissOnTouchBackground(): Boolean = true

    init {
        setOnClickListener {
            dismiss()
        }
    }

}
