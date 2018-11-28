package com.twt.service.ecard.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardPref
import com.twt.service.ecard.model.login
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class EcardLoginActivity : AppCompatActivity() {

    private lateinit var etStudentNum: EditText
    private lateinit var etPassword: EditText
    private lateinit var btBind: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        enableLightStatusBarMode(true)

        setContentView(R.layout.ecard_activity_bind)

        etStudentNum = findViewById(R.id.et_ecard_num)
        etPassword = findViewById(R.id.et_ecard_password)
        btBind = findViewById(R.id.btn_ecard_bind)

        if (EcardPref.ecardUserName != "")
            etStudentNum.setText(EcardPref.ecardUserName)

        btBind.setOnClickListener {
            val studentNum = etStudentNum.text.toString()
            val password = etPassword.text.toString()

            when {
                studentNum.isEmpty() -> Toasty.error(this@EcardLoginActivity, "请输入学号").show()
                password.isEmpty() -> Toasty.error(this@EcardLoginActivity, "请输入密码").show()
                else -> {
                    val deferred = async(CommonPool) {
                        //                        login(studentNum, password)
                        EcardPref.ecardUserName = studentNum
                        EcardPref.ecardPassword = password
                    }
                    launch(UI) {
                        deferred.await()
                    }
                    Toasty.success(this@EcardLoginActivity, "校园卡绑定成功").show()
                    finish()
                }
            }
        }
    }
}
