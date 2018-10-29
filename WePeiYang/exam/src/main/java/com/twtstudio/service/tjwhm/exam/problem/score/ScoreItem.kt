package com.twtstudio.service.tjwhm.exam.problem.score

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat
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
import com.twtstudio.service.tjwhm.exam.commons.toSelectionIndex
import com.twtstudio.service.tjwhm.exam.problem.*
import com.twtstudio.service.tjwhm.exam.user.addCollection
import com.twtstudio.service.tjwhm.exam.user.deleteCollection
import com.twtstudio.service.tjwhm.exam.user.star.StarActivity
import es.dmoral.toasty.Toasty
import okhttp3.MultipartBody
import org.jetbrains.anko.layoutInflater
import android.content.Intent
import android.net.Uri


/**
 * Created by tjwhm@TWTStudio at 9:27 PM,2018/8/7.
 * Happy coding!
 */

class ScoreItem(val index: Int, val context: Context, val testProblemBean: TestProblemBean, val resultBean: ResultBean) : Item {
    override val controller: ItemController
        get() = Controller

    private var isCollected = false

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_score, parent, false))

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as ScoreItem
            holder.apply {
                tvProblemTitle?.text = (item.index + 1).toString() + "." + Html.fromHtml(item.testProblemBean.content).toString()
                tvRightAnswer?.text = "正确答案：${item.resultBean.true_answer}"
                if (item.resultBean.answer == "")
                    tvUserAnswer?.text = "未做"
                else
                    tvUserAnswer?.text = "你的答案：${item.resultBean.answer}"
                if (item.resultBean.is_true == 1) tvUserAnswer?.setTextColor(ContextCompat.getColor(item.context, R.color.examTextBlue))
                else tvUserAnswer?.setTextColor(ContextCompat.getColor(item.context, R.color.examTextRed))
                rvScoreSelections?.layoutManager = LinearLayoutManager(item.context)
                rvScoreSelections?.withItems {
                    repeat(item.testProblemBean.option.size) {
                        selectionItem(null, it.toSelectionIndex(), item.testProblemBean.option[it], SelectionItem.NONE)
                    }
                }
                item.isCollected = item.resultBean.is_collected == 1

                if (item.isCollected)
                    ivStar?.setImageResource(R.drawable.exam_ic_star_filled)
                else
                    ivStar?.setImageResource(R.drawable.exam_ic_star_blank)


                ivStar?.setOnClickListener { _ ->
                    if (!item.isCollected) {
                        val list = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("ques_type", item.resultBean.ques_type.toString())
                                .addFormDataPart("ques_id", item.resultBean.ques_id.toString())
                                .build()
                                .parts()
                        addCollection(StarActivity.STAR.toString(), list) {
                            when (it) {
                                is RefreshState.Failure -> Toasty.error(ivStar.context, "网络错误").show()
                                is RefreshState.Success -> {
                                    Toasty.success(ivStar.context, "收藏成功").show()
                                    ivStar.setImageResource(R.drawable.exam_ic_star_filled)
                                    item.isCollected = true
                                }
                            }
                        }
                    } else {
                        val list = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("ques_type", item.resultBean.ques_type.toString())
                                .addFormDataPart("ques_id", item.resultBean.ques_id.toString())
                                .build()
                                .parts()
                        deleteCollection(StarActivity.STAR.toString(), list) {
                            when (it) {
                                is RefreshState.Failure -> Toasty.error(ivStar.context, "网络错误").show()
                                is RefreshState.Success -> {
                                    Toasty.success(ivStar.context, "取消收藏").show()
                                    ivStar.setImageResource(R.drawable.exam_ic_star_blank)
                                    item.isCollected = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvProblemTitle: TextView? = itemView?.findViewById(R.id.tv_score_item_problem_title)
        val rvScoreSelections: RecyclerView? = itemView?.findViewById(R.id.rv_score_item_selections)
        val tvRightAnswer: TextView? = itemView?.findViewById(R.id.tv_score_right_answer)
        val tvUserAnswer: TextView? = itemView?.findViewById(R.id.tv_score_user_answer)
        val ivStar: ImageView? = itemView?.findViewById(R.id.iv_score_star)
    }
}

fun MutableList<Item>.scoreItem(index: Int, context: Context, testProblemBean: TestProblemBean, resultBean: ResultBean) = add(ScoreItem(index, context, testProblemBean, resultBean))
