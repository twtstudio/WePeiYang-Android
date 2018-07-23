package com.twtstudio.service.tjwhm.exam.list

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.problem.NoneAnimatorListener
import com.twtstudio.service.tjwhm.exam.problem.ProblemActivity
import org.jetbrains.anko.layoutInflater

class LessonItem(val context: Context, val lessonData: LessonData) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                LessonViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_list, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as LessonViewHolder
            item as LessonItem
            holder.apply {
                tvEnterContest?.visibility = View.GONE
                tvEnterPractice?.visibility = View.GONE
                tvTitle?.text = item.lessonData.course_name
                itemView.setOnClickListener {
                    if (isExpand) {
                        tvEnterContest?.apply {
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

                        tvEnterContest?.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate()?.translationYBy(16f)
                                    ?.alpha(1f)
                                    ?.setDuration(200)
                                    ?.setListener(NoneAnimatorListener)
                        }
                        tvEnterPractice?.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate()?.translationYBy(16f)
                                    ?.alpha(1f)
                                    ?.setDuration(200)
                                    ?.setListener(NoneAnimatorListener)
                        }
                        ivExpend?.animate()
                                ?.rotation(90.0f)
                        isExpand = true
                    }
                }

                val intent = Intent(item.context, ProblemActivity::class.java)
                intent.putExtra(ProblemActivity.CLASS_ID_KEY, item.lessonData.id)
                tvEnterPractice?.setOnClickListener {
                    intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.READ_AND_PRACTICE)
                    item.context.startActivity(intent)
                }

                tvEnterContest?.setOnClickListener {
                    intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.CONTEST)
                    item.context.startActivity(intent)
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
        val tvEnterContest: TextView? = itemView?.findViewById(R.id.tv_enter_contest)
        val tvEnterPractice: TextView? = itemView?.findViewById(R.id.tv_enter_practice)
    }
}

fun MutableList<Item>.lessonItem(context: Context, lessonData: LessonData) = add(LessonItem(context, lessonData))