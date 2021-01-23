package com.twtstudio.retrox.auth.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.startActivity
import com.twtstudio.retrox.auth.R
import com.twtstudio.retrox.auth.api.getCaptcha
import com.twtstudio.retrox.auth.api.register
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.coroutines.experimental.asReference

class RegisterActivity : AppCompatActivity() {
    private lateinit var student_id: EditText
    private lateinit var usernameEt: EditText
    private lateinit var phone_number: EditText
    private lateinit var captcha: EditText
    private lateinit var captchaBtn: Button
    private lateinit var passwordEt: EditText
    private lateinit var email: EditText
    private lateinit var personal_id: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginPb: ProgressBar
    private lateinit var privacy: TextView
    private lateinit var privacy_checkbox: CheckBox
    private var infoCompleted: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_register)

        enableLightStatusBarMode(true)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorCompat(R.color.white)
        }

        initComponents()
        val infoList = listOf(student_id, usernameEt, phone_number, captcha, passwordEt, email, personal_id)

        captchaBtn = findViewById<Button>(R.id.btn_verify).apply {
            setOnClickListener {
                val activity = this@RegisterActivity.asReference()
                if (phone_number.text.isBlank()) {
                    Toasty.error(this@RegisterActivity, "请填写手机号码").show()
                } else {
                    getCaptcha(phoneNumber = phone_number.text.toString()) {
                        when (it) {
                            is RefreshState.Success ->
                                Toasty.success(activity(), "发送成功").show()

                            is RefreshState.Failure -> {
                                Log.d("captchahahahah","${it.throwable.message}！")
                                Toasty.error(activity(), "${it.throwable.message}！").show()
                                registerBtn.isEnabled = true
                            }
                        }
                    }
                }
            }
        }


        registerBtn = findViewById<Button>(R.id.btn_register).apply {
            setOnClickListener {
                hideSoftInputMethod()
                val activity = this@RegisterActivity.asReference()
                for (e: EditText in infoList) {
                    if (e.text.isBlank()) {
                        infoCompleted = false
                        break
                    }
                }

                if (!privacy_checkbox.isChecked) {
                    Toasty.error(this@RegisterActivity, "请同意隐私政策").show()
                }

                if (infoCompleted) {
                    loginPb.visibility = View.VISIBLE
                    registerBtn.isEnabled = false
                    register(studentId = student_id.text.toString(),
                            username = usernameEt.text.toString(),
                            phoneNumber = phone_number.text.toString(),
                            captcha = captcha.text.toString(),
                            password = passwordEt.text.toString(),
                            email = email.text.toString(),
                            personalID = personal_id.text.toString()) {
                        when (it) {
                            is RefreshState.Success -> {
                                Toasty.success(activity(), "注册成功").show()
                                CommonPreferences.isUserInfoUpdated = true
                            }

                            is RefreshState.Failure -> {
                                Toasty.error(activity(), "${it.throwable.message}！").show()
                                loginPb.visibility = View.INVISIBLE
                                registerBtn.isEnabled = true
                            }
                        }

                    }
                } else {
                    Toasty.error(this@RegisterActivity, "请完善个人信息").show()
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
        student_id = findViewById(R.id.et_student_id)
        usernameEt = findViewById(R.id.et_username)
        phone_number = findViewById(R.id.et_phone_number)
        captcha = findViewById(R.id.et_captcha)
        passwordEt = findViewById(R.id.et_password)
        email = findViewById(R.id.et_email)
        personal_id = findViewById(R.id.et_person_id)

        privacy = findViewById<TextView>(R.id.tv_privacy).apply {
            setOnClickListener {
                CommonContext.application.startActivity("privacy")
            }
        }
        privacy_checkbox = findViewById(R.id.privacy_check)
        loginPb = findViewById(R.id.pb_login)

    }
}