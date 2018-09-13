package com.twtstudio.retrox.tjulibrary.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.fitSystemWindowWithStatusBar
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.Datax

class DetialAcitvity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter :DetialConditionAdapter
    lateinit var book_name : TextView
    lateinit var book_author : TextView
    lateinit var book_content : TextView
    lateinit var book_publish_date : TextView
    lateinit var book_publisher : TextView
    lateinit var book_pic : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val indexOfBook = bundle.getString("indexOfBook")
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detial)
        toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar).also {
            fitSystemWindowWithStatusBar(it)
            setSupportActionBar(it)
        }
        setSupportActionBar(toolbar)
        toolbar.title = "借阅数据"

        book_name = findViewById(R.id.book_name)
        book_author = findViewById(R.id.book_author)
        book_content = findViewById(R.id.book_content)
        book_publish_date = findViewById(R.id.book_publish_date)
        book_publisher = findViewById(R.id.book_publisher)
        book_pic = findViewById(R.id.book_pic)
        recyclerView = findViewById(R.id.book_recyclerview)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

    }

    fun setDetial (data : Datax) {
        book_name.text = data.title
        var author = ""

        for (i in 0..((data.authorPrimary).size - 1)) {
            author += data.authorPrimary[i]
            if (i != (data.authorPrimary.size - 1)) author += "，"
        }
        book_author.text = author
        book_publisher.text = data.publisher
        book_publish_date.text = data.year
        adapter = DetialConditionAdapter(data.holding, this)
        recyclerView.adapter = adapter
    }

    fun setIntroduction (text : String) {
        book_content.text = text
    }

    
}