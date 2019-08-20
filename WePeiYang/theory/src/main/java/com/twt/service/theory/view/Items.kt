package com.twt.service.theory.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.experimental.theme.CustomTheme.context
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.padding

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

class ExamSingleAnswerItem() : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamSingleAnswerItemViewHolder
            item as ExamSingleAnswerItem
            holder.apply {
                singles.setOnCheckedChangeListener(null)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_examques_single, parent, false)
            return ExamSingleAnswerItemViewHolder(view)
        }


    }

    private class ExamSingleAnswerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.theory_ques_title_s)
        val singles = itemView.findViewById<RadioGroup>(R.id.theory_ques_radiogroup)
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
                    // TODO()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_examques_single, parent, false)
            return ExamMultiAnswerItemViewHolder(view)
        }


    }

    private class ExamMultiAnswerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.theory_ques_title_m)
        val multis = itemView.findViewById<LinearLayout>(R.id.theory_ques_checkgroup)
    }


    override val controller: ItemController
        get() = ExamMultiAnswerItem
}

fun MutableList<Item>.setMessage() = add(MessageItem())

fun MutableList<Item>.setExamItem(titles: String, detail: String, examFragment: ExamFragment) = add(ExamItem(titles, detail, examFragment))

fun MutableList<Item>.setProfileItem() = add(ProfileItem())

fun MutableList<Item>.setUserExamItem() = add(UserExamItem())

fun MutableList<Item>.setDetail(titles: String, detail: String) = add(ExamDetailItem(titles, detail))

fun MutableList<Item>.setSingleAnsQues() = add(ExamSingleAnswerItem())

fun MutableList<Item>.setMultiAnsQues(num: Int = 4) = add(ExamMultiAnswerItem(num))


