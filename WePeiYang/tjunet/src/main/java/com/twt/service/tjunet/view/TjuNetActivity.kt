package com.twt.service.tjunet.view

import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import com.twt.service.tjunet.R
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.service.tjunet.reconnect.ReconnectJob
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick


/**
 * Created by retrox on 2018/3/13.
 */
class TjuNetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#FFAEB9")

        relativeLayout {

            padding = dip(8)
            backgroundColor = Color.parseColor("#FFFFFF")
            toolbar {
                backgroundColor = Color.parseColor("#FFAEB9")
                textView("上网") {
                    textColor = Color.parseColor("#FFFFFF")
                }
            }.lparams(dip(1000), dip(40)) {
                leftPadding = dip(0)
                topPadding = dip(0)
                rightPadding = dip(0)
            }

            imageView {
                image = resources.getDrawable(R.drawable.wode)
            }.lparams(width = dip(30), height = dip(30)) {
                setMargins(200, 200, 200, 270)
            }


            val username = editText(TjuNetPreferences.username) {
                hint = "用户名"
                background = resources.getDrawable(R.drawable.et_bg)
                //isCursorVisible=false


            }.lparams(dip(180), wrapContent) {
                setMargins(300, 200, 170, 270)

            }
            imageView {
                image = resources.getDrawable(R.drawable.mima)
            }.lparams(dip(30), dip(30)) {
                setMargins(200, 380, 200, 0)
            }

            val password = editText(TjuNetPreferences.password) {
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
                hint = "密码"
                background = resources.getDrawable(R.drawable.et_bg)
                // isCursorVisible=false

            }.lparams(dip(180), wrapContent) {
                setMargins(300, 360, 170, 0)
            }


            checkBox("断开时尝试自动重连") {
                isChecked = TjuNetPreferences.autoConnect
                onCheckedChange { buttonView, isChecked ->
                    TjuNetPreferences.autoConnect = isChecked
                    if (isChecked) {
                        Toasty.success(this@TjuNetActivity, "已设置自动登录，自动重连在MIUI华为等后台严格的机型可能会失效").show()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val jobinfo = ReconnectJob.getScheduler(this@TjuNetActivity)
                            val jobService = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                            jobService.schedule(jobinfo)
                        }
                    } else {
                        Toasty.info(this@TjuNetActivity, "已关闭自动登录").show()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val jobService = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                            jobService.cancel(ReconnectJob.jobId)
                            jobService.cancelAll()
                        }
                    }
                }
            }.lparams() {
                setMargins(230, 500, 200, 0)
            }


            val status = textView {
                text = "当前状态-> 欢迎使用一键上网"
            }.lparams {
                setMargins(250, 600, 200, 0)
            }

            button("Login") {

                // ResourcesCompat.getDrawable(,R.drawable.values,null)
                background = resources.getDrawable(R.drawable.values)
                alpha = 0.5f
                textColor = Color.parseColor("#FFFFFF")
                gravity = Gravity.CENTER
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
            }.lparams(dip(270), dip(50)) {
                setMargins(300, 700, 300, 0)

            }

            button("Logout") {
                background = resources.getDrawable(R.drawable.values)
                alpha = 0.5f
                textColor = Color.parseColor("#FFFFFF")
                gravity = Gravity.CENTER
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
            }.lparams(dip(270), dip(50)) {
                setMargins(300, 870, 300, 0)

            }

            button("IP") {
                background = resources.getDrawable(R.drawable.values)
                alpha = 0.5f
                textColor = Color.parseColor("#FFFFFF")
                gravity = Gravity.CENTER
                onClick {
                    val result = RealTjuNetService.getIp().awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    result?.data?.let {
                        status.text = it.toString()
                    }
                }
            }.lparams(dip(270), dip(50)) {
                setMargins(300, 1040, 300, 0)

            }

            button("Status") {
                background = resources.getDrawable(R.drawable.values)
                //backgroundColor = Color.parseColor("#FF7777")
                alpha = 0.5f
                textColor = Color.parseColor("#FFFFFF")
                gravity = Gravity.CENTER
                onClick {
                    val result = RealTjuNetService.getStatus().awaitAndHandle {
                        Toasty.error(this@TjuNetActivity, "好像出了什么问题，${it.message}").show()
                    }
                    result?.data?.let {
                        status.text = it.toString()
                    }
                }
            }.lparams(dip(270), dip(50)) {
                setMargins(300, 1210, 300, 0)

            }

            button("自服务") {
                background = resources.getDrawable(R.drawable.values)
                //backgroundColor = Color.parseColor("#FF7777")
                alpha = 0.5f
                textColor = Color.parseColor("#FFFFFF")
                gravity = Gravity.CENTER
                onClick {
                    val uri = Uri.parse("http://g.tju.edu.cn ")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }.lparams(dip(270), dip(50)) {
                setMargins(300, 1380, 300, 700)

            }
        }

    }

}