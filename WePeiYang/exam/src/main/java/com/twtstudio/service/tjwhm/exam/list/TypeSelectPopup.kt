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

class TypeSelectPopup(mContext: Context, private val listActivityInterface: ListActivityInterface?, private val startXY: Pair<Float, Float>, private val lessonID: Int, private val showTest: Boolean) : BlurPopupWindow(mContext), LifecycleOwner {

    constructor(context: Context) : this(context, null, Pair(0.0f, 0.0f), 0, false)

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    lateinit var view: View

    private lateinit var tvLesson: TextView
    private lateinit var tvSingle: TextView
    private lateinit var tvSingleEnter: TextView
    private lateinit var tvMulti: TextView
    private lateinit var tvMultiEnter: TextView
    private lateinit var tvTf: TextView
    private lateinit var tvTfEnter: TextView
    private lateinit var tvTestTitle: TextView
    private lateinit var tvTestDesc: TextView
    private lateinit var tvTestEnter: TextView

    val density = context.resources.displayMetrics.density

    override fun createContentView(parent: ViewGroup): View = parent.context.layoutInflater
            .inflate(R.layout.exam_popup_type_select, parent, false).apply {
                layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER
                    horizontalMargin = dip(24)
                }
            }.also {
                view = it
                tvLesson = view.findViewById(R.id.tv_type_select_lesson_title)
                tvSingle = view.findViewById(R.id.tv_type_select_single_title)
                tvSingleEnter = view.findViewById(R.id.tv_type_select_single_enter)
                tvMulti = view.findViewById(R.id.tv_type_select_multi_title)
                tvMultiEnter = view.findViewById(R.id.tv_type_select_multi_enter)
                tvTf = view.findViewById(R.id.tv_type_select_tf_title)
                tvTfEnter = view.findViewById(R.id.tv_type_select_tf_enter)
                tvTestTitle = view.findViewById(R.id.tv_type_select_test_title)
                tvTestDesc = view.findViewById(R.id.tv_type_select_test_title_desc)
                tvTestEnter = view.findViewById(R.id.tv_type_select_test_enter)
            }

    @SuppressLint("SetTextI18n")
    override fun onShow() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        if (!showTest) {
            tvTestTitle.visibility = View.GONE
            tvTestDesc.visibility = View.GONE
            tvTestEnter.visibility = View.GONE
        }
        getLessonInfo(lessonID.toString()) { it ->
            when (it) {
                is RefreshState.Failure -> Toasty.error(context, "网络错误").show()
                is RefreshState.Success -> {
                    it.message.data!!.apply {
                        tvLesson.text = course_name
                        if (single_num.toInt() != 0)
                            tvSingle.text = "单选题   $single_done_count/$single_num   当前: ${single_ques_index + 1}"
                        else {
                            tvSingle.text = "单选题   无题目"
                            tvSingleEnter.visibility = View.INVISIBLE
                        }
                        if (multi_num.toInt() != 0)
                            tvMulti.text = "多选题   $multi_done_count/$multi_num   当前: ${multi_ques_index + 1}"
                        else {
                            tvMulti.text = "多选题   无题目"
                            tvMultiEnter.visibility = View.INVISIBLE
                        }
                        if (decide_num.toInt() != 0)
                            tvTf.text = "判断题   $decide_done_count/$decide_num   当前: ${decide_ques_index + 1}"
                        else {
                            tvTf.text = "判断题   无题目"
                            tvTfEnter.visibility = View.INVISIBLE
                        }
                        val intent = Intent(context, ProblemActivity::class.java).apply {
                            putExtra(ProblemActivity.LESSON_ID_KEY, lessonID)
                        }
                        when {
                            single_num.toInt() == 0 -> {
                                tvSingleEnter.visibility = View.INVISIBLE
                            }
                            single_done_count == 0 -> {
                                tvSingleEnter.apply {
                                    text = "开始练习"
                                    setOnClickListener {
                                        intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                                        intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.SINGLE_CHOICE)
                                        context.startActivity(intent)
                                        listActivityInterface?.initList()
                                        dismiss()
                                    }
                                }
                            }
                            else -> tvSingleEnter.apply {
                                text = "继续练习"
                                setOnClickListener {
                                    intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                                    intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.SINGLE_CHOICE)
                                    intent.putExtra(ProblemActivity.CONTINUE_INDEX_KEY, single_ques_index)
                                    context.startActivity(intent)
                                    listActivityInterface?.initList()
                                    dismiss()
                                }

                            }
                        }
                        when {
                            multi_num.toInt() == 0 -> {
                                tvMultiEnter.visibility = View.GONE
                            }
                            multi_done_count == 0 -> {
                                tvMultiEnter.apply {
                                    text = "开始练习"
                                    setOnClickListener {
                                        intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                                        intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.MULTI_CHOICE)
                                        context.startActivity(intent)
                                        listActivityInterface?.initList()
                                        dismiss()
                                    }
                                }
                            }
                            else -> tvMultiEnter.apply {
                                text = "继续练习"
                                setOnClickListener {
                                    intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                                    intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.MULTI_CHOICE)
                                    intent.putExtra(ProblemActivity.CONTINUE_INDEX_KEY, multi_ques_index)
                                    context.startActivity(intent)
                                    listActivityInterface?.initList()
                                    dismiss()
                                }
                            }
                        }
                        when {
                            decide_num.toInt() == 0 -> {
                                tvTfEnter.visibility = View.GONE
                            }
                            decide_done_count == 0 -> {
                                tvTfEnter.apply {
                                    text = "开始练习"
                                    setOnClickListener {
                                        intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                                        intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.TRUE_FALSE)
                                        context.startActivity(intent)
                                        listActivityInterface?.initList()
                                        dismiss()
                                    }
                                }

                            }
                            else -> tvTfEnter.apply {
                                text = "继续练习"
                                setOnClickListener {
                                    intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                                    intent.putExtra(ProblemActivity.PROBLEM_TYPE_KEY, ProblemActivity.TRUE_FALSE)
                                    intent.putExtra(ProblemActivity.CONTINUE_INDEX_KEY, decide_ques_index)
                                    context.startActivity(intent)
                                    listActivityInterface?.initList()
                                    dismiss()
                                }
                            }
                        }
                        tvTestEnter.setOnClickListener {
                            intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.CONTEST)
                            context.startActivity(intent)
                            listActivityInterface?.initList()
                            dismiss()
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
