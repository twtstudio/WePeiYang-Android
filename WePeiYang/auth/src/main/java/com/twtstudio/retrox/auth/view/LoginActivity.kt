package com.twtstudio.retrox.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.tencent.stat.StatMultiAccount
import com.tencent.stat.StatService
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator.REMOTE
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.startActivity
import com.twtstudio.retrox.auth.R
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.auth.api.login
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.alert
import org.jetbrains.anko.coroutines.experimental.asReference


/**
 * Created by retrox on 2016/11/27.
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var loginPb: ProgressBar
    private lateinit var privacy: TextView
    private lateinit var privacy_checkbox: CheckBox
    private lateinit var register_btn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_login)

        enableLightStatusBarMode(true)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorCompat(R.color.white)
        }

        usernameEt = findViewById(R.id.et_username)
        passwordEt = findViewById(R.id.et_password)
        privacy = findViewById<TextView>(R.id.tv_privacy).apply {
            setOnClickListener {
                CommonContext.application.startActivity("privacy")
            }
        }
        privacy_checkbox = findViewById(R.id.privacy_check)

//        register_btn = findViewById<TextView>(R.id.tv_register).apply {
//            setOnClickListener {
//                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//            }
//        }

        loginPb = findViewById(R.id.pb_login)
        loginBtn = findViewById<Button>(R.id.btn_login).apply {
            setOnClickListener {
                hideSoftInputMethod()
                val activity = this@LoginActivity.asReference()
                if (!privacy_checkbox.isChecked) {
                    Toasty.error(this@LoginActivity, "请同意隐私政策").show()
                } else if (usernameEt.text.isBlank()) {
                    Toasty.error(this@LoginActivity, "请输入用户名").show()
                } else if (passwordEt.text.isBlank()) {
                    Toasty.error(this@LoginActivity, "请输入密码").show()
                } else {
                    loginPb.visibility = View.VISIBLE
                    loginBtn.isEnabled = false

                    login(usernameEt.text.toString(), passwordEt.text.toString()) {
                        when (it) {
                            is RefreshState.Success ->
                                authSelfLiveData.refresh(REMOTE) {
                                    when (it) {
                                        is RefreshState.Success -> {
                                            Toasty.success(activity(), "登录成功").show()
                                            startActivity(name = "welcome")

                                            // 腾讯 MTA 账号统计
                                            val account = StatMultiAccount(StatMultiAccount.AccountType.CUSTOM, CommonPreferences.twtuname)

                                            // 登录时间，单位为秒
                                            val time = System.currentTimeMillis() / 1000
                                            account.lastTimeSec = time

                                            // 因为现在做了自动重新登录，所以过期时间相当于没有，暂时设置为两年 (365 * 2)，每天 24 小时，每小时 3600 秒
                                            account.expireTimeSec = time + 365 * 2 * 24 * 3600
                                            StatService.reportMultiAccount(context, account)

                                            finish()
                                        }
                                        is RefreshState.Failure -> {
                                            Toasty.error(activity(), "发生错误 ${it.throwable.message}").show()
                                            loginPb.visibility = View.INVISIBLE
                                            loginBtn.isEnabled = true
                                        }
                                    }
                                }
                            is RefreshState.Failure -> {
                                if (it.throwable.message.equals("请前往信息完善界面完善手机号等相关信息")) {
                                    alert {
                                        title = "账户信息不完整"
                                        message = "请前往信息完善界面完善账户信息"
                                        positiveButton("去完善") {
                                            val intent = Intent(this@LoginActivity, InfoSuppleActivity::class.java)
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                                            startActivity(intent)
                                        }
//                                        negativeButton("算了") {
//                                            startActivity(name = "welcome")
//                                        }
                                    }.show()
                                } else {
                                    Toasty.error(activity(), "${it.throwable.message}！").show()
                                    loginPb.visibility = View.INVISIBLE
                                    loginBtn.isEnabled = true
                                }

                            }
                        }
                    }
                }
            }
        }

    }


    private fun hideSoftInputMethod() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.apply {
            hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }
}
