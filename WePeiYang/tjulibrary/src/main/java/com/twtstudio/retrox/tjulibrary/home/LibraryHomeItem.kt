package com.twtstudio.retrox.tjulibrary.home

import android.arch.lifecycle.LifecycleOwner
import android.graphics.Color
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.matchParent

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
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent).apply {
                    horizontalPadding = dip(16)
                }
            }
            homeItem.apply {
                itemName.text = "LIBRARY"
                itemContent.visibility = View.INVISIBLE
                setContentView(view)
            }
            return MyViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MyViewHolder
            item as LibraryHomeItem
            val itemManager = (holder.recyclerView.adapter as ItemAdapter).itemManager
            LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
            LibraryViewModel.infoLiveData.bindNonNull(item.owner) { info ->
                itemManager.refreshAll {
                    if (info.books == null) info.books = listOf()
                    info.books = info.books.sortedBy { it.timeLeft() }
                    if (collasped) {
                        info.books.take(3).forEach {
                            book(it)
                        }
                        if (info.books.size > 3) {
                            lightText("${info.books.size - 3}本书被折叠 点击显示") {
                                setOnClickListener {
                                    collasped = false
                                    LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL)
                                }
                            }
                        } else lightText("无更多书显示 点击刷新") {
                            setOnClickListener {
                                LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL)
                            }
                        }
                    } else {
                        info.books.forEach {
                            book(it)
                        }
                        lightText("点击折叠图书") {
                            setOnClickListener {
                                collasped = true
                                LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL)
                            }
                        }
                    }
                }
                holder.homeItem.itemContent.apply {
                    val contentText = "借书 <span style=\"color:#AE837B\";>${info.books.size} </span> 即将到期 <span style=\"color:#E70C57\";>${info.books.count { it.timeLeft() < 8 }}</span>".spanned
                    text = contentText
                }

            }
        }

        private fun MutableList<Item>.book(book: Book) = add(BookItem(book))

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
                    in 8..40 -> colorCircleView.color = Color.parseColor("#748165") // blue
                    in 3..7 -> colorCircleView.color = Color.parseColor("#DBB86B") // yellow
                    else -> colorCircleView.color = Color.parseColor("#ae837b") //red
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
            val rootView: View get() = itemView
        }
    }

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    override fun areContentsTheSame(newItem: Item): Boolean {
        return if (newItem is BookItem) {
            newItem.book == book
        } else false
    }

    override val controller: ItemController
        get() = Controller
}
