package com.twtstudio.service.tjwhm.exam.list

import android.animation.Animator
import android.animation.ValueAnimator
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import kotlinx.android.synthetic.main.exam_item_list.view.*
import org.jetbrains.anko.layoutInflater

class LessonItem(lifecycleOwner: LifecycleOwner, val context: Context) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return LessonViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_list, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as LessonViewHolder
            holder.apply {
                tvEnterExam?.visibility = View.GONE
                tvEnterPractice?.visibility = View.GONE
                itemView.setOnClickListener {
                    if (isExpand) {
                        tvEnterExam?.apply {
                            animate()?.translationY(-16f)
                                    ?.alpha(0f)
                                    ?.setDuration(200)
                                    ?.setListener(object : Animator.AnimatorListener {
                                        override fun onAnimationEnd(animation: Animator?) {
                                            visibility = View.GONE
                                        }

                                        override fun onAnimationCancel(animation: Animator?) = Unit
                                        override fun onAnimationStart(animation: Animator?) = Unit
                                        override fun onAnimationRepeat(animation: Animator?) = Unit
                                    })
                        }
                        tvEnterPractice?.apply {
                            animate()?.translationY(-16f)
                                    ?.alpha(0f)
                                    ?.setListener(object : Animator.AnimatorListener {
                                        override fun onAnimationEnd(animation: Animator?) {
                                            visibility = View.GONE
                                        }

                                        override fun onAnimationCancel(animation: Animator?) = Unit
                                        override fun onAnimationStart(animation: Animator?) = Unit
                                        override fun onAnimationRepeat(animation: Animator?) = Unit
                                    })
                        }
                        ivExpend?.animate()?.rotation(0f)
                        isExpand = false

                    } else {

                        tvEnterExam?.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate()
                                    ?.translationYBy(16f)
                                    ?.alpha(1f)
                                    ?.setDuration(200)
                                    ?.setListener(object : Animator.AnimatorListener {
                                        override fun onAnimationEnd(animation: Animator?) = Unit
                                        override fun onAnimationCancel(animation: Animator?) = Unit
                                        override fun onAnimationStart(animation: Animator?) = Unit
                                        override fun onAnimationRepeat(animation: Animator?) = Unit
                                    })
                        }
                        tvEnterPractice?.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate()?.translationYBy(16f)
                                    ?.alpha(1f)
                                    ?.setDuration(200)
                                    ?.setListener(object : Animator.AnimatorListener {
                                        override fun onAnimationEnd(animation: Animator?) = Unit
                                        override fun onAnimationCancel(animation: Animator?) = Unit
                                        override fun onAnimationStart(animation: Animator?) = Unit
                                        override fun onAnimationRepeat(animation: Animator?) = Unit
                                    })
                        }
                        ivExpend?.animate()
                                ?.rotation(90.0f)

                        isExpand = true

                    }
                }
            }
        }

    }

    override val controller: ItemController
        get() = Controller

    private class LessonViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var isExpand = false
        val tvTitle: TextView? = itemView?.findViewById(R.id.tv_list_lesson_title)
        val ivExpend: ImageView? = itemView?.findViewById(R.id.iv_list_expend)
        val tvEnterExam: TextView? = itemView?.findViewById(R.id.tv_enter_exam)
        val tvEnterPractice: TextView? = itemView?.findViewById(R.id.tv_enter_practice)
        val vDivider: View? = itemView?.findViewById(R.id.v_lesson_divider)
    }
}

fun MutableList<Item>.lessonItem(lifecycleOwner: LifecycleOwner, context: Context) = add(LessonItem(lifecycleOwner, context))