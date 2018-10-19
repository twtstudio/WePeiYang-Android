package com.twtstudio.retrox.tjulibrary.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.*

class DetailActivity : AppCompatActivity(),LibDetailModel.setBook {
    override fun setSearchBook(book: Book, url: String, total: TotalNum) {
        if (book.data.summary != null) {
            if (book.data.summary.length > MAX_NUM) {
                val x = book.data.summary.substring(0, MAX_NUM) + "..."
                book_content.text = x
            } else {
                book_content.text = book.data.summary
            }
        }
        Glide.with(this@DetailActivity)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.src)
                .error(R.drawable.src2)
                .into(book_pic)

        book_name.text = book.data.title
        var author = ""
        for (i in 0..((book.data.authorPrimary).size - 1)) {
            author += book.data.authorPrimary[i]
            if (i != (book.data.authorPrimary.size - 1)) author += "，"
        }

        book_author.text = author
        book_publisher.text = book.data.publisher
        book_publish_date.text = book.data.year
        adapter = DetailConditionAdapter(book.data.holding, this)
        recyclerView.adapter = adapter
        total_borrow_num.text = total.totalBorrowNum.toString()
    }

    val MAX_NUM = 150
    lateinit var bookDetail : BookDetail
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DetailConditionAdapter
    lateinit var book_name: TextView
    lateinit var book_author: TextView
    lateinit var book_content: TextView
    lateinit var book_publish_date: TextView
    lateinit var book_publisher: TextView
    lateinit var book_pic: ImageView
    lateinit var total_borrow_num: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lib_activity_detail)
        toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar).also {
            title = "借阅数据"
            it.setBackgroundColor(Color.parseColor("#e78fae"))

            setSupportActionBar(it)
        }
        window.statusBarColor = Color.parseColor("#e78fae")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val bundle = intent.extras
        val id = bundle.getString("id")
        book_name = findViewById(R.id.book_name)
        book_author = findViewById(R.id.book_author)
        book_content = findViewById(R.id.book_content)
        book_publish_date = findViewById(R.id.book_publish_date)
        book_publisher = findViewById(R.id.book_publisher)
        book_pic = findViewById(R.id.book_pic)
        recyclerView = findViewById(R.id.book_recyclerview)
        total_borrow_num = findViewById(R.id.total_borrow_num)
        bookDetail = BookDetail(this)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        bookDetail.getSearch(id)


    }

}
