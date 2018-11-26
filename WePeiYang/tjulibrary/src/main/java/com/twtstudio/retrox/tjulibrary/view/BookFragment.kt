package com.twtstudio.retrox.tjulibrary.view


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.provider.Book
import org.jetbrains.anko.textColor

class BookFragment : Fragment() {
    lateinit var bookName: TextView
    lateinit var bookArtist: TextView
    lateinit var bookImg: ImageView
    lateinit var bookPublish: TextView
    lateinit var bookBorrow: TextView
    lateinit var bookLeft: TextView
    lateinit var bookimg: String
    lateinit var bookpublish: String
    lateinit var book: Book


    companion object {
        fun newInstance(book: Book, bookImg: String, bookPublish: String): BookFragment {
            val args = Bundle()
            args.putSerializable("book", book)
            args.putString("book_publish", bookPublish)
            args.putString("book_img", bookImg)
            val fragment = BookFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.lib_home_cardview_detail, container, false)
        val bundle = arguments
        bookpublish = bundle!!.getString("book_publish")
        bookimg = bundle.getString("book_img")
        book = bundle.getSerializable("book") as Book
        bookImg = view.findViewById(R.id.home_image)
        bookName = view.findViewById(R.id.home_bookname)
        bookArtist = view.findViewById(R.id.detail_writer)
        bookPublish = view.findViewById(R.id.detail_publish)
        bookBorrow = view.findViewById(R.id.borrow)
        bookLeft = view.findViewById(R.id.detail_left)
        if (book.title.length > 20) {
            book.title = book.title.substring(0, 20) + "..."
        }
        bookName.text = book.title
        if (book.author.length > 14) {
            book.author = book.author.substring(0, 14) + "..."
        }
        bookArtist.text = book.author
        bookPublish.text = bookpublish
        bookBorrow.text = book.loanTime
        if (book.timeLeft() > 0) {
            bookLeft.text = book.timeLeft().toString() + "天"
        } else {
            val x = Math.abs(book.timeLeft())

            bookLeft.apply {
                textColor = Color.RED
                text = x.toString() + "(逾期)"
            }
        }
        Glide.with(activity)
                .load(bookimg)
                .asBitmap()
                .placeholder(R.drawable.lib_notfound)
                .error(R.drawable.lib_notfound)
                .into(bookImg)
        return view

    }

}