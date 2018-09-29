package com.twtstudio.retrox.tjulibrary.view


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

class BookFragment : Fragment() {
    lateinit var bookName : TextView
    lateinit var bookArtist : TextView
    lateinit var bookImg : ImageView
    lateinit var bookPublish : TextView
    lateinit var bookBorrow : TextView
    lateinit var bookLeft : TextView
    lateinit var bookname : String
    lateinit var bookauthor : String
    lateinit var bookborrow : String
    lateinit var bookleft : String
    lateinit var bookimg : String
    lateinit var bookpublish: String


    companion object {

        fun newInstance(book: Book,bookImg : String,bookPublish : String): BookFragment {
            val args = Bundle()
            args.putString("book_publish",bookPublish)
            args.putString("book_img",bookImg)
            args.putString("book_name", book.title)
            args.putString("book_author",book.author)
            args.putString("book_borrow",book.loanTime)
            args.putString("book_left",book.timeLeft().toString())
            val fragment = BookFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.lb_home_cardview_detail,container,false)
        var bundle = arguments
        bookpublish = bundle!!.getString("book_publish")
        bookimg = bundle.getString("book_img")
        bookname = bundle.getString("book_name")
        bookauthor = bundle.getString("book_author")
        bookborrow = bundle.getString("book_borrow")
        bookleft = bundle.getString("book_left")
        bookImg = view.findViewById(R.id.home_image)
        bookName = view.findViewById(R.id.home_bookname)
        bookArtist = view.findViewById(R.id.detail_writer)
        bookPublish = view.findViewById(R.id.detail_publish)
        bookBorrow = view.findViewById(R.id.borrow)
        bookLeft = view.findViewById(R.id.detail_left)
        if (bookname.length>10){
            bookname=bookname.substring(0,10)+"..."
        }
        bookName.text = bookname
        if (bookauthor.length>8){
            bookauthor = bookauthor.substring(0,8)+"..."
        }
        bookArtist.text = bookauthor
        bookPublish.text = bookpublish
        bookBorrow.text = bookborrow
        bookLeft.text = bookleft + "天"
        Glide.with(activity)
                .load(bookimg)
                .asBitmap()
                .placeholder(R.drawable.src)
                .error(R.drawable.src)
                .into(bookImg)







        return view

    }

}