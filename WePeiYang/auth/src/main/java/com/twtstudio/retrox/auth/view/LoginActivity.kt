package com.twtstudio.retrox.auth.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.twt.wepeiyang.commons.experimental.extensions.consume
import com.twt.wepeiyang.commons.experimental.startActivity
import com.twtstudio.retrox.auth.R
import com.twtstudio.retrox.auth.service.AuthProvider
import es.dmoral.toasty.Toasty


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
                AuthProvider.login(usernameEt.text.toString(), passwordEt.text.toString())
                loginBtn.isEnabled = false
                loginPb.visibility = View.VISIBLE
            }
        }

        AuthProvider.successLiveData.consume(this, from = AuthProvider.FROM_LOGIN) {
            Toasty.success(this, it).show()
            AuthProvider.authSelf()
        }

        AuthProvider.successLiveData.consume(this, from = AuthProvider.FROM_AUTH_SELF) {
            Toasty.success(this, it).show()
            loginPb.visibility = View.INVISIBLE
            startActivity(name = "welcome")
            finish()
        }

        AuthProvider.errorLiveData.consume(this) {
            Toasty.error(this, it).show()
            loginPb.visibility = View.INVISIBLE
            loginBtn.isEnabled = true
        }

    }
}
