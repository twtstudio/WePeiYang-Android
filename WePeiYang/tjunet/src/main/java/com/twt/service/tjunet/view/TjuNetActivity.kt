package com.twt.service.tjunet.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.PasswordTransformationMethod
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.api.TjuNetService
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by retrox on 2018/3/13.
 */
class TjuNetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            padding = dip(8)
            toolbar {
                title = "上网"
            }
            val username = editText(TjuNetPreferences.username) {
                hint = "用户名"
            }
            val password = editText(TjuNetPreferences.password) {
                transformationMethod = PasswordTransformationMethod.getInstance()
                hint = "密码"
            }
            val status = textView {
                text = "请登录"
            }

            button("Login") {
                onClick {
                    TjuNetPreferences.apply {
                        this.username = username.text.toString()
                        this.password = password.text.toString()
                    }
                    val body = RealTjuNetService.loginPost(
                            username = username.text.toString(),
                            password = password.text.toString()
                    ).awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    body?.string()?.let {
                        status.text = it
                    }
                }
            }

            button("Logout烂的") {
                onClick {
                    val body = RealTjuNetService.logoutPost(
                            username = username.text.toString(),
                            password = password.text.toString()
                    ).awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    body?.string()?.let {
                        status.text = it
                    }
                }
            }

            button("IP") {
                onClick {
                    val result = RealTjuNetService.getIp().awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    result?.data?.let {
                        status.text = it.toString()
                    }
                }
            }

            button("Status") {
                onClick {
                    val result = RealTjuNetService.getStatus().awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    result?.data?.let {
                        status.text = it.toString()
                    }
                }
            }
        }
    }
}