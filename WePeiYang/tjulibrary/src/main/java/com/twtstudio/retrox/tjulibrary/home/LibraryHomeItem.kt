package com.twtstudio.retrox.tjulibrary.home

import android.arch.lifecycle.LifecycleOwner
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.*
import com.twt.wepeiyang.commons.ui.spanned
import com.twt.wepeiyang.commons.ui.view.ColorCircleView
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.provider.Book
import com.twtstudio.retrox.tjulibrary.view.BookPopupWindow
import kotlinx.android.synthetic.main.item_library_book_new.view.*
import org.jetbrains.anko.*
import kotlin.properties.Delegates

class LibraryHomeItem(val owner: LifecycleOwner) : Item {
    companion object Controller : ItemController {
        var collasped = true
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = RecyclerView(parent.context)
            view.apply {
                layoutManager = LinearLayoutManager(parent.context)
                adapter = ItemAdapter(ItemManager())
                itemAnimator = DefaultItemAnimator()
                layoutParams = FrameLayout.LayoutParams(wrapContent, matchParent).apply {
                    horizontalPadding = dip(16)
                }
            }
            homeItem.apply {
                itemName.text = "LIBRARY"
                setContentView(view)
            }
            return MyViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MyViewHolder
            item as LibraryHomeItem
            val lifecycleOwner = item.owner
            val itemManager = (holder.recyclerView.adapter as ItemAdapter).itemManager
            LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
            LibraryViewModel.infoLiveData.bindNonNull(item.owner) { info ->
                itemManager.refreshAll {
                    if (collasped) {
                        info.books.take(3).forEach {
                            book(it)
                        }
                        if (info.books.size > 3) {
                            libsingleText("${info.books.size - 3}本书被折叠 点击显示") {
                                setOnClickListener {
                                    collasped = false
                                    LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL)
                                }
                            }
                        } else libsingleText("无更多书显示 点击刷新") {
                            setOnClickListener {
                                LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL)
                            }
                        }
                    } else {
                        info.books.forEach {
                            book(it)
                        }
                        libsingleText("点击折叠图书") {
                            setOnClickListener {
                                collasped = true
                                LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL)
                            }
                        }
                    }
                }
                holder.homeItem.itemContent.apply {
                    val contentText = "借书 <span style=\"color:#E70C57\";>${info.books.size} </span> 即将到期 <span style=\"color:#E70C57\";>${info.books.count { it.timeLeft() < 4 }}</span>".spanned
                    text = contentText
                }

            }
        }

        fun MutableList<Item>.book(book: Book) = add(BookItem(book))

        private class MyViewHolder(itemView: View, val homeItem: HomeItem, val recyclerView: RecyclerView) : RecyclerView.ViewHolder(itemView)

    }

    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.libraryHomeItem(owner: LifecycleOwner) = add(LibraryHomeItem(owner))

class BookItem(val book: Book) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_library_book_new, parent, false)
            return ViewHolder(view, view.tv_book_name, view.color_circle_book, view.tv_book_return)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as BookItem
            holder.apply {
                bookName.text = item.book.title
                bookReturn.text = "还书日期：${item.book.returnTime}"
                when (item.book.timeLeft()) {
                    in 8..40 -> colorCircleView.color = Color.parseColor("#3BCBFF") // blue
                    in 3..7 -> colorCircleView.color = Color.parseColor("#FFC017") // yellow
                    else -> colorCircleView.color = Color.parseColor("#FF5D64") //red
                }
                rootView.setOnClickListener {
                    val pop = BookPopupWindow(item.book, it.context)
                    pop.show()
                }
                rootView.setOnLongClickListener {
                    val pop = BookPopupWindow(item.book, it.context)
                    pop.show()
                    true
                }

            }
        }

        private class ViewHolder(itemView: View, val bookName: TextView, val colorCircleView: ColorCircleView, val bookReturn: TextView) : RecyclerView.ViewHolder(itemView) {
            val rootView get() = itemView
        }
    }

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    override fun areContentsTheSame(newItem: Item): Boolean {
        if (newItem is BookItem) {
            return newItem.book == book
        } else return false
    }

    override val controller: ItemController
        get() = Controller
}

class LibSingleTextItem(val text: String, val builder: TextView.() -> Unit = {}) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            var textView: TextView by Delegates.notNull()
            val view = parent.context.frameLayout {
                textView = textView {
                    textSize = 12f
                    textColor = Color.parseColor("#B9B9B9")
                    typeface = Typeface.create("sans-serif-regular", Typeface.NORMAL)
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.apply {
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                    bottomMargin = dip(8)
                }
            }
            return ViewHolder(view, textView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as LibSingleTextItem
            holder.textView.text = item.text
            holder.textView.apply(item.builder)
        }

        private class ViewHolder(itemView: View?, val textView: TextView) : RecyclerView.ViewHolder(itemView)

    }

    override fun areContentsTheSame(newItem: Item): Boolean = areItemsTheSame(newItem)

    override fun areItemsTheSame(newItem: Item): Boolean {
        if (newItem is LibSingleTextItem) {
            return newItem.text == text
        } else return false
    }

    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.libsingleText(text: String) = add(LibSingleTextItem(text))
fun MutableList<Item>.libsingleText(text: String, builder: TextView.() -> Unit) = add(LibSingleTextItem(text, builder))
