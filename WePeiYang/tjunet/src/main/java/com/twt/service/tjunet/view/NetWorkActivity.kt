package com.twt.service.tjunet.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.twt.service.tjunet.api.RealNetWorkService
import com.twt.service.tjunet.pref.NetworkPreferences
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import okhttp3.ResponseBody
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by retrox on 2018/3/13.
 */
class NetWorkActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            padding = dip(8)
            toolbar {
                title = "上网"
            }
            val username = editText(NetworkPreferences.username) {

            }
            val password = editText(NetworkPreferences.password) {
                transformationMethod = PasswordTransformationMethod.getInstance()
                hint = "Your PasswordHere"
            }
            val textview = textView {
                text = "Status"
            }

            button("Login") {
                onClick {
                    NetworkPreferences.apply {
                        this.password = password.text.toString()
                        this.username = username.text.toString()
                    }
                    val body = RealNetWorkService.loginPost(username = username.text.toString(), password = password.text.toString()).awaitAndHandle {
                        it.printStackTrace()
                    }
                    textview.text = body.string()
                }
            }

            button("LogOut烂的") {
                onClick {
                    val body = RealNetWorkService.logoutPost(username = username.text.toString(), password = password.text.toString()).awaitAndHandle {
                        it.printStackTrace()
                    }
                    textview.text = body.string()
                }
            }
        }
    }
}