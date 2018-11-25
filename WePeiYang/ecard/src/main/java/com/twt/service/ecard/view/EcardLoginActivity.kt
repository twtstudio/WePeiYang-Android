package com.twt.service.ecard.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.twt.service.ecard.model.EcardPref
import com.twt.service.ecard.model.login
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.button
import org.jetbrains.anko.editText
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class EcardLoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            textView {
                text = "当前校园卡内测阶段，账号和密码都只会被加密存储到本地，所有数据都通过本地模拟请求的方式获取，不会经过天外天服务器"
            }
            val usernameEditText = editText {
                hint = "校园卡账号（学号）"
                if (EcardPref.ecardUserName != "") setText(EcardPref.ecardUserName)
            }

            val passwordEditText = editText {
                hint = "校园卡密码（圈存机上输入的密码）"
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
                if (EcardPref.ecardPassword != "") setText(EcardPref.ecardPassword)
            }

            val loginButton = button {
                text = "LOGIN"
                onClick {
                    val username = usernameEditText.text.toString()
                    val password = passwordEditText.text.toString()

                    val deferred = async(CommonPool) {
                        login(username, password)
                        EcardPref.ecardUserName = username
                        EcardPref.ecardPassword = password
                    }
                    deferred.await()
                    Toasty.success(this@EcardLoginActivity, "校园卡绑定成功").show()
                }
            }
        }
    }
}