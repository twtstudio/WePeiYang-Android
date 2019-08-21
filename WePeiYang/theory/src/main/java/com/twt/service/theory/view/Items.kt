package com.twt.service.theory.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import anim.ExamDetailActivity
import com.bumptech.glide.Glide
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.experimental.theme.CustomTheme.context
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.padding
import android.R.attr.button
import android.support.annotation.IdRes
import android.util.Log
import java.lang.Math.pow


class ProfileItem : Item {

    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ProfileItemViewHolder
            item as ProfileItem
            holder.apply {
                name.text = "张三"
                account.text = "我爱天外天"
                number.text = "3019216001"
                Glide.with(context)
                        .load("http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0856d58c8b40e499be90efdff7fe43827ee42ba09")
                        .into(avatar)

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_userinfo, parent, false)

            return ProfileItemViewHolder(view)
        }
    }

    private class ProfileItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.theory_user_name)
        val account = itemView.findViewById<TextView>(R.id.theory_user_account)
        val number = itemView.findViewById<TextView>(R.id.theory_user_number)
        val avatar = itemView.findViewById<ImageView>(R.id.theory_user_avatar)
    }

    override val controller: ItemController
        get() = ProfileItem


}


class ExamItem(val titles: String, val detail: String, val examFragment: ExamFragment) : Item {

    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamItemViewHolder
            item as ExamItem
            holder.apply {
                name.text = item.titles
                content.text = item.detail
                itemView.setOnClickListener {
                    val bundle = Bundle()
//                    bundle.putString("id", item.id)
                    val intent = Intent()
                    intent.putExtras(bundle)
                    intent.setClass(item.examFragment.context, ExamDetailActivity::class.java)
                    item.examFragment.startActivity(intent)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_exam, parent, false)

            return ExamItemViewHolder(view)
        }


    }

    private class ExamItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.eaxm_name)
        val content = itemView.findViewById<TextView>(R.id.eaxm_detail)
    }

    override val controller: ItemController
        get() = ExamItem

}

class UserExamItem : Item {

    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as UserExamItemViewHolder
            item as UserExamItem
            holder.apply {
                name.text = "形势与政策第三十七期"
                date.text = "2019.04.29 — 2019.05.05"
                score.text = "100"
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_user_exam, parent, false)

            return UserExamItemViewHolder(view)
        }


    }

    private class UserExamItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.theory_exam_name)
        val date = itemView.findViewById<TextView>(R.id.theory_exam_date)
        val ctg = itemView.findViewById<TextView>(R.id.theory_exam_ctg)
        val status = itemView.findViewById<TextView>(R.id.theory_exam_status)
        val score = itemView.findViewById<TextView>(R.id.theory_user_score)
    }

    override val controller: ItemController
        get() = UserExamItem

}

class MessageItem : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MessageItemViewHolder
            item as MessageItem
            holder.apply {
                name.text = "一次小考试"
                content.text = "一次小考试"
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_announce, parent, false)

            return MessageItemViewHolder(view)
        }


    }

    private class MessageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.eaxm_announce_name)
        val content = itemView.findViewById<TextView>(R.id.eaxm_announce_detail)
    }

    override val controller: ItemController
        get() = MessageItem
}

class ExamDetailItem(val titles: String, val detail: String) : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamDetailItemViewHolder
            item as ExamDetailItem

            holder.apply {
                title.text = item.titles
                times.text = item.detail
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_examdetail, parent, false)

            return ExamDetailItemViewHolder(view)
        }


    }

    private class ExamDetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.eaxm_detail_name)
        val times = itemView.findViewById<TextView>(R.id.eaxm_detail_detail)
    }


    override val controller: ItemController
        get() = ExamDetailItem
}

class ExamSingleAnswerItem : Item {
    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_examques_single, parent, false)
            return ExamSingleAnswerItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamSingleAnswerItemViewHolder
            item as ExamSingleAnswerItem
            holder.apply {
                singles.apply {
                    setOnCheckedChangeListener(null)
                    clearCheck()
                    val saved_ans = AnswerManager.getAnswer(position + 1)
                    if (saved_ans != 0) {
                        check(getChildAt(saved_ans - 1).id)
                    }
                    setOnCheckedChangeListener { radioGroup, i ->
                        var num = 0
                        repeat(4) {
                            // 只有四个选项
                            if (getChildAt(it).id == i)
                                num = it + 1
                        }
                        AnswerManager.update(position + 1, num)

                    }
                }
            }
        }

    }


    private class ExamSingleAnswerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.theory_ques_title_s)
        val singles = itemView.findViewById<RadioGroup>(R.id.theory_ques_radiogroup)
        val ansA = itemView.findViewById<RadioButton>(R.id.theory_ques_ansA)
        val ansB = itemView.findViewById<RadioButton>(R.id.theory_ques_ansB)
        val ansC = itemView.findViewById<RadioButton>(R.id.theory_ques_ansC)
        val ansD = itemView.findViewById<RadioButton>(R.id.theory_ques_ansD)
    }


    override val controller: ItemController
        get() = ExamSingleAnswerItem
}

class ExamMultiAnswerItem(val num: Int = 4) : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamMultiAnswerItemViewHolder
            item as ExamMultiAnswerItem
            holder.apply {
                multis.apply {
                    var saved_ans = AnswerManager.getAnswer(position + 1)

                    repeat(6) {
                        // 清空复用可能导致的错误
                        (this.getChildAt(it) as CheckBox).setOnCheckedChangeListener(null)
                        (this.getChildAt(it) as CheckBox).isChecked = false
                        if (saved_ans % 10 == 1) {
                            (this.getChildAt(it) as CheckBox).isChecked = true
                            saved_ans /= 10
                        }
                    }
                    repeat(6) {
                        // 设置监听和题目
                        if (it >= item.num) {
                            this.getChildAt(it).visibility = View.GONE
                        } else {
                            (this.getChildAt(it) as CheckBox).setOnCheckedChangeListener { compoundButton, b ->
                                var value = AnswerManager.getAnswer(position + 1)
                                if (!b) {       // 答案以形式上的二进制保存
                                    value -= pow(10.toDouble(), it.toDouble()).toInt()
                                    AnswerManager.update(position + 1, value)
                                } else {
                                    value += pow(10.toDouble(), it.toDouble()).toInt()
                                    AnswerManager.update(position + 1, value)
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_examques_multi, parent, false)
            return ExamMultiAnswerItemViewHolder(view)
        }


    }

    private class ExamMultiAnswerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.theory_ques_title_m)
        val multis = itemView.findViewById<LinearLayout>(R.id.theory_ques_checkgroup)
        val ansA = itemView.findViewById<CheckBox>(R.id.theory_ques_ansA_m)
        val ansB = itemView.findViewById<CheckBox>(R.id.theory_ques_ansB_m)
        val ansC = itemView.findViewById<CheckBox>(R.id.theory_ques_ansC_m)
        val ansD = itemView.findViewById<CheckBox>(R.id.theory_ques_ansD_m)
        val ansE = itemView.findViewById<CheckBox>(R.id.theory_ques_ansE_m)
        val ansF = itemView.findViewById<CheckBox>(R.id.theory_ques_ansF_m)
    }


    override val controller: ItemController
        get() = ExamMultiAnswerItem
}

class ProblemItem(val id: Int, var done: Boolean) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_circle_layout, parent, false)

            return ProblemItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ProblemItemViewHolder
            item as ProblemItem
            holder.idTextView.text = item.id.toString()
            if (item.done) {
                holder.idTextView.setTextColor(Color.parseColor("#ffffff"))
                holder.bg.background = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.theory_circle_a)
            } else {
                holder.idTextView.setTextColor(Color.parseColor("#1e90ff"))
                holder.bg.background = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.theory_circle_b)
            }
        }
    }

    private class ProblemItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView = itemView.findViewById<TextView>(R.id.idTextView)
        val bg = itemView.findViewById<ConstraintLayout>(R.id.backGround)
    }

    override val controller: ItemController
        get() = ProblemItem

}

fun MutableList<Item>.setProblemItem(id: Int, done: Boolean) = add(ProblemItem(id, done))

fun MutableList<Item>.setMessage() = add(MessageItem())

fun MutableList<Item>.setExamItem(titles: String, detail: String, examFragment: ExamFragment) = add(ExamItem(titles, detail, examFragment))

fun MutableList<Item>.setProfileItem() = add(ProfileItem())

fun MutableList<Item>.setUserExamItem() = add(UserExamItem())

fun MutableList<Item>.setDetail(titles: String, detail: String) = add(ExamDetailItem(titles, detail))

fun MutableList<Item>.setSingleAnsQues() = add(ExamSingleAnswerItem())

fun MutableList<Item>.setMultiAnsQues(num: Int = 4) = add(ExamMultiAnswerItem(num))