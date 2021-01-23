package com.twtstudio.retrox.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.startActivity
import com.twtstudio.retrox.auth.R
import com.twtstudio.retrox.auth.api.getCaptcha
import com.twtstudio.retrox.auth.api.infoSupplement
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.coroutines.experimental.asReference

class InfoSuppleActivity : AppCompatActivity() {
    private lateinit var phone_number: EditText
    private lateinit var captcha: EditText
    private lateinit var captchaBtn: Button
    private lateinit var email: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginPb: ProgressBar
    private var infoCompleted: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_infosupple)

        enableLightStatusBarMode(true)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorCompat(R.color.white)
        }

        initComponents()
        val infoList = listOf(phone_number, captcha, email)

        captchaBtn = findViewById<Button>(R.id.btn_verify).apply {
            setOnClickListener {
                val activity = this@InfoSuppleActivity.asReference()
                if (phone_number.text.isBlank()) {
                    Toasty.error(this@InfoSuppleActivity, "请填写手机号码").show()
                } else {
                    getCaptcha(phoneNumber = phone_number.text.toString()) {
                        when (it) {
                            is RefreshState.Success ->
                                Toasty.success(activity(), "发送成功").show()

                            is RefreshState.Failure -> {
                                Log.d("captchahahahah", "${it.throwable.message}！")
                                Toasty.error(activity(), "${it.throwable.message}！").show()
                                registerBtn.isEnabled = true
                            }
                        }
                    }
                }
            }
        }


        registerBtn = findViewById<Button>(R.id.btn_supplement).apply {
            setOnClickListener {
                hideSoftInputMethod()
                val activity = this@InfoSuppleActivity.asReference()
                for (e: EditText in infoList) {
                    if (e.text.isBlank()) {
                        infoCompleted = false
                        break
                    }
                }

                if (infoCompleted) {
                    loginPb.visibility = View.VISIBLE
                    registerBtn.isEnabled = false
                    infoSupplement(phoneNumber = phone_number.text.toString(),
                            captcha = captcha.text.toString(),
                            email = email.text.toString()
                            ) {
                        when (it) {
                            is RefreshState.Success -> {
                                Toasty.success(activity(), "完善成功").show()
                                CommonPreferences.isUserInfoUpdated = true
                                startActivity(name = "welcome")
                            }

                            is RefreshState.Failure -> {
                                Toasty.error(activity(), "${it.throwable.message}！").show()
                                loginPb.visibility = View.INVISIBLE
                                registerBtn.isEnabled = true
                            }
                        }

                    }
                } else {
                    Toasty.error(this@InfoSuppleActivity, "请完善个人信息").show()
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

    private fun initComponents() {
        phone_number = findViewById(R.id.et_phone_number)
        captcha = findViewById(R.id.et_captcha)
        email = findViewById(R.id.et_email)
        loginPb = findViewById(R.id.pb_login)
    }
}