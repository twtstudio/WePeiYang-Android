package com.tjuwhy.yellowpages2.view

import android.animation.ObjectAnimator
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tjuwhy.yellowpages2.R
import com.tjuwhy.yellowpages2.service.GroupData
import com.tjuwhy.yellowpages2.service.SearchBean
import com.tjuwhy.yellowpages2.service.update
import com.tjuwhy.yellowpages2.utils.Expandable
import com.tjuwhy.yellowpages2.utils.FIRST_INDEX_KEY
import com.tjuwhy.yellowpages2.utils.SECOND_INDEX_KEY
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.regex.Matcher
import java.util.regex.Pattern

class HeaderItem(val context: Context) : Item {

    companion object Controller : ItemController {
        override fun onBindViewHolder(holder: android.support.v7.widget.RecyclerView.ViewHolder, item: Item) {
            item as HeaderItem
            holder as HeaderViewHolder
            val viewList = arrayListOf(holder.tju1895, holder.libIv,
                    holder.hospitalIv, holder.dormitoryIv, holder.bikeIv, holder.teamIv, holder.bankIv, holder.fixIv)
            viewList.forEachIndexed { index, imageView ->
                imageView.setOnClickListener {
                    startActivity(item.context, index)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): android.support.v7.widget.RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.yp2_item_home_header, parent, false)
            val tju1895 = view.findViewById<ImageView>(R.id.food)
            val libIv = view.findViewById<ImageView>(R.id.lib)
            val hospitalIv = view.findViewById<ImageView>(R.id.hospital)
            val dormitoryIv = view.findViewById<ImageView>(R.id.dormitory)
            val bikeIv = view.findViewById<ImageView>(R.id.bike)
            val teamIv = view.findViewById<ImageView>(R.id.team)
            val bankIv = view.findViewById<ImageView>(R.id.bank)
            val fixIv = view.findViewById<ImageView>(R.id.fix)
            return HeaderViewHolder(view, tju1895, libIv, hospitalIv, dormitoryIv, bikeIv, teamIv, bankIv, fixIv)
        }

        fun startActivity(context: Context, index: Int) {
            val intent = Intent(context, DepartmentActivity::class.java)
            when (index) {
                0 -> addExtra(intent, 2, 18)
                1 -> addExtra(intent, 2, 0)
                2 -> addExtra(intent, 2, 2)
                3 -> addExtra(intent, 0, 15)
                4 -> addExtra(intent, 2, 19)
                5 -> addExtra(intent, 0, 20)
                6 -> addExtra(intent, 2, 20)
                7 -> addExtra(intent, 0, 14)
            }
            context.startActivity(intent)
        }

    }

    class HeaderViewHolder(itemView: View, val tju1895: ImageView, val libIv: ImageView, val hospitalIv: ImageView,
                           val dormitoryIv: ImageView, val bikeIv: ImageView, val teamIv: ImageView, val bankIv: ImageView,
                           val fixIv: ImageView) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController = Controller

}

class GroupItem(val groupData: GroupData, val expandable: Expandable) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as GroupViewHolder
            item as GroupItem
            holder.groupTitle.text = item.groupData.title
            holder.itemView.setOnClickListener {
                if (item.groupData.isExpanded) {
                    item.expandable.collapse(item.groupData.groupIndex)
                    item.groupData.isExpanded = false
                    ObjectAnimator.ofFloat(holder.arrowIv, "rotation", 90f, 0f).setDuration(500).start()
                } else {
                    item.expandable.expand(item.groupData.groupIndex)
                    item.groupData.isExpanded = true
                    ObjectAnimator.ofFloat(holder.arrowIv, "rotation", 0f, 90f).setDuration(500).start()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = parent.context.layoutInflater
            val view = layoutInflater.inflate(R.layout.yp2_item_home_group, parent, false)
            val arrowIv = view.findViewById<ImageView>(R.id.state_arrow)
            val groupTitle = view.findViewById<TextView>(R.id.group_name)
            return GroupViewHolder(view, arrowIv, groupTitle)
        }

    }

    class GroupViewHolder(itemView: View, val arrowIv: ImageView, val groupTitle: TextView) : RecyclerView.ViewHolder(itemView)
}

class SubItem(val context: Context, val name: String, val groupIndex: Int, val childIndex: Int, val firstIndex: Int) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = parent.context.layoutInflater
            val view = layoutInflater.inflate(R.layout.yp2_item_home_sub, parent, false)
            val textView = view.findViewById<TextView>(R.id.sub_text)
            return ViewHolder(view, textView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as SubItem
            holder.textView.text = item.name
            holder.itemView.setOnClickListener {
                val intent = Intent(item.context, DepartmentActivity::class.java)
                intent.putExtra(FIRST_INDEX_KEY, item.firstIndex - 1)
                intent.putExtra(SECOND_INDEX_KEY, item.childIndex)
                item.context.startActivity(intent)
            }
        }

    }

    class ViewHolder(itemView: View, val textView: TextView) : RecyclerView.ViewHolder(itemView)
}

class ChildItem(val context: Context, val name: String, val phoneNum: String, var isStared: Boolean, val tid: Int) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ChildViewHolder
            item as ChildItem
            holder.thirdName.text = item.name
            holder.phoneTv.text = item.phoneNum
            holder.isStared.apply {
                if (item.isStared) {
                    setImageResource(R.drawable.yp2_favorite_light)
                } else {
                    setImageResource(R.drawable.yp2_favourite_dark)
                }
                onClick {
                    update(item.tid) { refreshState, str ->
                        when (refreshState) {
                            is RefreshState.Success -> {
                                if (item.isStared) {
                                    Toasty.success(item.context, str, Toast.LENGTH_SHORT).show()
                                    item.isStared = false
                                    holder.isStared.setImageResource(R.drawable.yp2_favourite_dark)
                                } else {
                                    Toasty.success(item.context, str, Toast.LENGTH_SHORT).show()
                                    item.isStared = true
                                    holder.isStared.setImageResource(R.drawable.yp2_favorite_light)
                                }
                            }
                            is RefreshState.Failure -> {
                                Toasty.error(item.context, "$str，请检查网络")
                            }
                        }
                    }
                }
            }

            holder.phoneIv.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phoneNum}"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                item.context.startActivity(intent)
            }
            holder.itemView.setOnClickListener {
                val items = arrayListOf("复制号码", "报错/反馈")
                val normalDialog = AlertDialog.Builder(item.context)
                normalDialog.setItems(items.toTypedArray()) { _, which ->
                    when (which) {
                        0 -> {
                            val cm = (item.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                            cm.text = item.phoneNum.trim()
                            Toasty.success(item.context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                        }
                        1 -> {
                            val normalDialog1 = AlertDialog.Builder(item.context)
                            normalDialog1.setMessage("号码/名称有误？是否要加入天外天用户社区群进行反馈？")
                                    .setPositiveButton("加吧") { _, _ ->
                                        val qq = "738068756"
                                        val url = "mqqwpa://im/chat?chat_type=group&uin=$qq&version=1"
                                        item.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    }
                                    .setNegativeButton("算了") { _, _ -> }
                            normalDialog1.show()
                        }
                    }
                }
                normalDialog.show()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.yp2_item_home_child, parent, false)
            val thirdName = view.findViewById<TextView>(R.id.third_name)
            val phoneTv = view.findViewById<TextView>(R.id.phone_tv)
            val isStared = view.findViewById<ImageView>(R.id.is_starred)
            val phoneIv = view.findViewById<ImageView>(R.id.phone_button)
            return ChildViewHolder(view, thirdName, phoneTv, isStared, phoneIv)
        }
    }

    class ChildViewHolder(itemView: View, val thirdName: TextView, val phoneTv: TextView, val isStared: ImageView, val phoneIv: ImageView) : RecyclerView.ViewHolder(itemView)
}

class CharItem(val a: Char) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.yp2_item_char, parent, false)
            val char = view.findViewById<TextView>(R.id.item_text_char)
            return ViewHolder(view, char)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as CharItem
            holder as ViewHolder
            holder.char.text = item.a.toString()
        }

    }

    class ViewHolder(itemView: View, val char: TextView) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller

}

class SearchHistoryItem(val context: Context, val str: String, val block: (String) -> Unit) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.yp2_item_search_history, parent, false)
            val text = view.findViewById<TextView>(R.id.history_text)
            return ViewHolder(view, text)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as SearchHistoryItem
            holder as ViewHolder
            holder.textView.text = item.str
            holder.itemView.setOnClickListener {
                item.block(item.str)
            }
        }
    }

    override val controller: ItemController
        get() = Controller

    class ViewHolder(itemView: View, val textView: TextView) : RecyclerView.ViewHolder(itemView)

}

class DeleteHistoryItem(val block: () -> Unit) : Item {

    companion object Controller : ItemController {

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as DeleteHistoryItem
            holder.itemView.setOnClickListener {
                item.block()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.yp2_item_delete_history, parent, false)
            return ViewHolder(view)
        }

    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller
}


class SearchResultItem(val context: Context, val searchBean: SearchBean, val query: String) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.yp2_item_search_result, parent, false)
            val textView = view.findViewById<TextView>(R.id.item_sear_result_text1)
            val textView2 = view.findViewById<TextView>(R.id.item_sear_result_text2)
            return ViewHolder(view, textView, textView2)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as SearchResultItem
            var content = ""
            val subList = item.searchBean.unit_list?.map { it.item_name }
            subList?.apply {
                if (this.size > 1) {
                    this.forEach {
                        content += "$it、"
                    }
                } else if (this.size == 1) {
                    content += this[0]
                }
            }
            holder.apply {
                textView2?.setSingleLine()
                textView?.text = matchText(item.searchBean.department_name, item.query)
                textView2?.text = matchText(content, item.query)
                itemView.setOnClickListener {
                    val intent = Intent(item.context, DepartmentActivity::class.java)
                    val id = item.searchBean.department_attach - 1
                    intent.putExtra(FIRST_INDEX_KEY, id)
                    intent.putExtra(SECOND_INDEX_KEY, when (id) {
                        0 -> item.searchBean.id - 1
                        1 -> item.searchBean.id - 29
                        2 -> item.searchBean.id - 54
                        else -> 0
                    })
                    item.context.startActivity(intent)
                }
            }
        }

        private fun matchText(text: String, keyword: String): SpannableString {
            val ss = SpannableString(text)
            val pattern: Pattern = Pattern.compile(keyword)
            val matcher: Matcher = pattern.matcher(ss)
            while (matcher.find()) {
                val start: Int = matcher.start()
                val end: Int = matcher.end()
                ss.setSpan(ForegroundColorSpan(Color.parseColor("#45a0e3")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return ss
        }
    }

    class ViewHolder(itemView: View?, val textView: TextView?, val textView2: TextView?) : RecyclerView.ViewHolder(itemView)
}

private fun addExtra(intent: Intent, firstIndex: Int, secondIndex: Int) {
    intent.putExtra(FIRST_INDEX_KEY, firstIndex)
    intent.putExtra(SECOND_INDEX_KEY, secondIndex)
}
