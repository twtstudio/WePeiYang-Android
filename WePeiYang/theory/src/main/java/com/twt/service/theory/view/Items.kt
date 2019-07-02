package com.twt.service.theory.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.experimental.theme.CustomTheme.context
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

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
                        .load("https://pp.myapp.com/ma_icon/0/icon_12114891_1557843941/96")
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


class ExamItem : Item {

    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamItemViewHolder
            item as ExamItem
            holder.apply {
                name.text = "一次小考试"
                content.text = "一次小考试"
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

class ExamDetailItem : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamDetailItemViewHolder
            item as ExamDetailItem

            holder.apply {
                title.text = "考试时间"
                times.text = "2019-4-20"
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

fun MutableList<Item>.setMessage() = add(MessageItem())

fun MutableList<Item>.setExamItem() = add(ExamItem())

fun MutableList<Item>.setProfileItem() = add(ProfileItem())

fun MutableList<Item>.setUserExamItem() = add(UserExamItem())