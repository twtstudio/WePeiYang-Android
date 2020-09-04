package com.twt.service.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import android.widget.Toast
import com.twt.service.R

import es.dmoral.toasty.Toasty


/**
 * Created by retrox on 01/04/2017.
 */

class PrivacyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Toasty.info(this, "加载中...", Toast.LENGTH_SHORT).show()
        setContentView(R.layout.activity_privacy)
        val privacy_policy = findViewById<TextView>(R.id.privacy_policy)
        privacy_policy.setMovementMethod(ScrollingMovementMethod.getInstance())
    }
}
