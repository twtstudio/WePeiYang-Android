package com.twtstudio.retrox.auth.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.twtstudio.retrox.auth.R
import com.twtstudio.retrox.auth.api.AuthProvider
import com.twtstudio.retrox.auth.ext.consume
import es.dmoral.toasty.Toasty


/**
 * Created by retrox on 2016/11/27.
 */

class LoginActivity : AppCompatActivity() {

    lateinit var usernameEt: EditText
    lateinit var passwordEt: EditText
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_login)

        usernameEt = findViewById(R.id.et_username)
        passwordEt = findViewById(R.id.et_password)
        loginBtn = findViewById<Button>(R.id.btn_login).apply {
            setOnClickListener {
                AuthProvider.login(usernameEt.text.toString(), passwordEt.text.toString())
            }
        }

        AuthProvider.successLiveData.consume(this, from = AuthProvider.LOGIN) {
            it?.let {
                Toasty.success(this, it).show()
                AuthProvider.authSelf()
            }
        }

        AuthProvider.successLiveData.consume(this, from = AuthProvider.AUTH_SELF) {
            it?.let {
                Toasty.success(this, it).show()
                val intent = Intent(
                        this,
                        Class.forName("com.twt.service.module.welcome.WelcomeSlideActivity")
                )
                startActivity(intent)
                finish()
            }
        }

        AuthProvider.errorLiveData.consume(this) {
            it?.let {
                Toasty.error(this, it).show()
            }
        }

    }
}
