package com.twt.service.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.mukesh.MarkdownView

import java.io.IOException

import es.dmoral.toasty.Toasty
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


/**
 * Created by retrox on 01/04/2017.
 */

class DevTalkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toasty.info(this, "加载中...", Toast.LENGTH_SHORT).show()

        val markdownView = MarkdownView(this@DevTalkActivity)
        setContentView(markdownView)

        val client = OkHttpClient()

        val request = Request.Builder()
                .url("https://raw.githubusercontent.com/life2015/WePeiYangDevTalking/master/README.md")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toasty.error(this@DevTalkActivity, "网络错误", Toast.LENGTH_SHORT).show()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                this@DevTalkActivity.runOnUiThread {
                    try {
                        markdownView.setMarkDownText(response.body()!!.string())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}
