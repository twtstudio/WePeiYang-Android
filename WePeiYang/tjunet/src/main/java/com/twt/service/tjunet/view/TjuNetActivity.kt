package com.twt.service.tjunet.view

import android.app.job.JobScheduler
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.api.TjuNetService
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.twt.service.tjunet.reconnect.ReconnectJob


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
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
                hint = "密码"
            }
            checkBox("断开时尝试自动重连") {
                isChecked = TjuNetPreferences.autoConnect
//                onCheckedChange { buttonView, isChecked ->
//                    TjuNetPreferences.autoConnect = isChecked
//                    if (isChecked) {
//                        Toasty.success(this@TjuNetActivity, "已设置自动登录，自动重连在MIUI华为等后台严格的机型可能会失效").show()
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            val jobinfo = ReconnectJob.getScheduler(this@TjuNetActivity)
//                            val jobService = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//                            jobService.schedule(jobinfo)
//                        }
//                    } else {
//                        Toasty.info(this@TjuNetActivity, "已关闭自动登录").show()
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            val jobService = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//                            jobService.cancel(ReconnectJob.jobId)
//                            jobService.cancelAll()
//                        }
//                    }
//                }
            }

            val status = textView {
                text = "当前状态-> 欢迎使用一键上网"
            }.lparams {
                topMargin = dip(8)
            }

            button("Login") {
                onClick {
                    TjuNetPreferences.apply {
                        this.username = username.text.toString()
                        this.password = password.text.toString()
                    }
                    val body = RealTjuNetService.login(
                            username = username.text.toString(),
                            password = password.text.toString()
                    ).awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    body?.let {
                        status.text = it.toString()
                    }
                }
            }

            button("Logout") {
                onClick {
                    val body = RealTjuNetService.logoutTry(
                            username = username.text.toString(),
                            password = password.text.toString()
                    ).awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    body?.let {
                        status.text = it.toString()
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

            button("自服务") {
                onClick {
                    val uri = Uri.parse("http://g.tju.edu.cn ")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        }
    }
}