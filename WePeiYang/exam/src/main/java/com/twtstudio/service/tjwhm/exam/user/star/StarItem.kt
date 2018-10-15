package com.twtstudio.service.tjwhm.exam.user.star

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.selectionIndexToInt
import com.twtstudio.service.tjwhm.exam.commons.toLessonType
import com.twtstudio.service.tjwhm.exam.commons.toProblemType
import com.twtstudio.service.tjwhm.exam.commons.toSelectionIndex
import com.twtstudio.service.tjwhm.exam.problem.ProblemBean
import com.twtstudio.service.tjwhm.exam.problem.SelectionItem
import com.twtstudio.service.tjwhm.exam.problem.selectionItem
import com.twtstudio.service.tjwhm.exam.user.addCollection
import com.twtstudio.service.tjwhm.exam.user.deleteCollection
import es.dmoral.toasty.Toasty
import okhttp3.MultipartBody
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 12:16 PM,2018/8/15.
 * Happy coding!
 */

class StarItem(val context: Context, val problemBean: ProblemBean, val starOrWrong: Int) : Item {
    override val controller: ItemController
        get() = Controller

    private var isMistake = problemBean.is_mistake == 1
    private var isCollected = problemBean.is_collected == 1

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_star, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as StarItem
            holder.apply {

                tvLessonType?.text = item.problemBean.class_id.toLessonType()
                tvProblemType?.text = item.problemBean.ques_type.toProblemType()
                tvProblemTitle?.text = Html.fromHtml(item.problemBean.content)
                tvAnswer?.text = "题目答案: ${item.problemBean.answer}"
                rvSelections?.layoutManager = LinearLayoutManager(item.context)

                when (item.starOrWrong) {
                    StarActivity.STAR -> {
                        rvSelections?.withItems {
                            for (i in 0 until item.problemBean.option.size)
                                selectionItem(null, i.toSelectionIndex(), item.problemBean.option[i], SelectionItem.NONE)
                        }
                        ivWrong?.visibility = View.GONE
                    }
                    StarActivity.WRONG -> {
                        rvSelections?.withItems {
                            for (i in 0 until item.problemBean.option.size)
                                when (i) {
                                    item.problemBean.error_option.selectionIndexToInt() -> selectionItem(null, i.toSelectionIndex(), item.problemBean.option[i], SelectionItem.FALSE)
                                    item.problemBean.answer.selectionIndexToInt() -> selectionItem(null, i.toSelectionIndex(), item.problemBean.option[i], SelectionItem.TRUE)
                                    else -> selectionItem(null, i.toSelectionIndex(), item.problemBean.option[i], SelectionItem.NONE)
                                }
                        }
                    }
                }

                ivStar?.setOnClickListener { _ ->
                    if (!item.isCollected) {
                        val list = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("ques_type", item.problemBean.ques_type)
                                .addFormDataPart("ques_id", item.problemBean.ques_id.toString())
                                .build()
                                .parts()
                        addCollection(StarActivity.STAR.toString(), list) {
                            when (it) {
                                is RefreshState.Failure -> Toasty.error(item.context, "网络错误").show()
                                is RefreshState.Success -> {
                                    Toasty.success(item.context, "收藏成功").show()
                                    ivStar.setImageResource(R.drawable.exam_ic_star_filled)
                                    item.isCollected = true
                                }
                            }
                        }
                    } else {
                        val list = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("ques_type", item.problemBean.ques_type)
                                .addFormDataPart("ques_id", item.problemBean.ques_id.toString())
                                .build()
                                .parts()
                        deleteCollection(StarActivity.STAR.toString(), list) {
                            when (it) {
                                is RefreshState.Failure -> Toasty.error(item.context, "网络错误").show()
                                is RefreshState.Success -> {
                                    Toasty.success(item.context, "取消收藏").show()
                                    ivStar.setImageResource(R.drawable.exam_ic_star_blank)
                                    item.isCollected = false
                                }
                            }
                        }
                    }
                }
                if (!item.isCollected) {
                    ivStar?.setImageResource(R.drawable.exam_ic_star_blank)
                }

                ivWrong?.setOnClickListener { _ ->
                    if (item.isMistake) {
                        val list = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("ques_type", item.problemBean.ques_type)
                                .addFormDataPart("ques_id", item.problemBean.ques_id.toString())
                                .build()
                                .parts()
                        deleteCollection(StarActivity.WRONG.toString(), list) {
                            when (it) {
                                is RefreshState.Failure -> Toasty.error(item.context, "网络错误").show()
                                is RefreshState.Success -> {
                                    Toasty.success(item.context, "删除成功").show()
                                    ivWrong.setImageResource(R.drawable.exam_ic_wrong_collection_blank)
                                    item.isMistake = false
                                }
                            }
                        }
                    } else {
                        val list = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("ques_type", item.problemBean.ques_type)
                                .addFormDataPart("ques_id", item.problemBean.ques_id.toString())
                                .addFormDataPart("error_answer", item.problemBean.error_option)
                                .build()
                                .parts()
                        addCollection(StarActivity.WRONG.toString(), list) {
                            when (it) {
                                is RefreshState.Failure -> Toasty.error(item.context, "网络错误").show()
                                is RefreshState.Success -> {
                                    Toasty.success(item.context, "重新加入错题本").show()
                                    ivWrong.setImageResource(R.drawable.exam_ic_wrong_collection_filled)
                                    item.isMistake = true
                                }
                            }
                        }
                    }
                }

                if (!item.isMistake) {
                    ivWrong?.setImageResource(R.drawable.exam_ic_wrong_collection_blank)
                }
            }
        }
    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvLessonType: TextView? = itemView?.findViewById(R.id.tv_star_lesson_type)
        val tvProblemType: TextView? = itemView?.findViewById(R.id.tv_star_problem_type)
        val tvProblemTitle: TextView? = itemView?.findViewById(R.id.tv_star_problem_title)
        val rvSelections: RecyclerView? = itemView?.findViewById(R.id.rv_star_selection)
        val tvAnswer: TextView? = itemView?.findViewById(R.id.tv_star_answer)
        val ivStar: ImageView? = itemView?.findViewById(R.id.iv_star_star)
        val ivWrong: ImageView? = itemView?.findViewById(R.id.iv_star_wrong)
    }
}

fun MutableList<Item>.starItem(context: Context, problemBean: ProblemBean, starOrWrong: Int) = add(StarItem(context, problemBean, starOrWrong))
