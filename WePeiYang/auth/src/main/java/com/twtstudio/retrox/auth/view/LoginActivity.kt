package com.twtstudio.retrox.auth.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator.REMOTE
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twtstudio.retrox.auth.R
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.auth.api.login
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.coroutines.experimental.asReference


/**
 * Created by retrox on 2016/11/27.
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var loginPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_login)

        usernameEt = findViewById(R.id.et_username)
        passwordEt = findViewById(R.id.et_password)
        loginPb = findViewById(R.id.pb_login)
        loginBtn = findViewById<Button>(R.id.btn_login).apply {
            setOnClickListener {
                loginBtn.isEnabled = false
                loginPb.visibility = View.VISIBLE
                hideSoftInputMethod()

                val activity = this@LoginActivity.asReference()
                login(usernameEt.text.toString(), passwordEt.text.toString()) {
                    when (it) {
                        is RefreshState.Success ->
                            authSelfLiveData.refresh(REMOTE) {
                                when (it) {
                                    is RefreshState.Success -> {
                                        Toasty.success(activity(), "登录成功").show()
                                        finish()
                                    }
                                    is RefreshState.Failure -> {
                                        Toasty.error(activity(), "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                        loginPb.visibility = View.INVISIBLE
                                        loginBtn.isEnabled = true
                                    }
                                }
                            }

                        is RefreshState.Failure -> {
                            Toasty.error(activity(), "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                            loginPb.visibility = View.INVISIBLE
                            loginBtn.isEnabled = true
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
