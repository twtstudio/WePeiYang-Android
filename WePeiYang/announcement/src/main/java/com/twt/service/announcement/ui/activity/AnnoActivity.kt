package com.twt.service.announcement.ui.activity

//import com.twt.service.announcement.service.AnnoService
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.twt.service.announcement.R
import com.twt.service.announcement.detail.DetailActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

class AnnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)


        GlobalScope.launch {
//            val string = AnnoService.Companion.get().awaitAndHandle{it.printStackTrace()}?.data.toString()
//            Log.d("tag_tree",string)
        }

        findViewById<FloatingActionButton>(R.id.fl_btn).apply {
            this.onClick {
                startActivity(
                        Intent(this@AnnoActivity, DetailActivity::class.java)
                                .putExtra("title", "如何看待天津大学补锌猫")
                                .putExtra("content", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                                // TODO: 这里应当传一个userId进来
                                .putExtra("status", 0)
                                .putExtra("time", "1895")
                                .putExtra("likeState", false)
                                .putExtra("likeCount", 114514)
                )
            }
        }


    }
}