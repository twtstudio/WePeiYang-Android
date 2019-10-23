package com.twt.service.settings

import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.network.RxErrorHandler
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.auth.api.login
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider
import es.dmoral.toasty.Toasty
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class SingleBindActivity : AppCompatActivity() {
    companion object {
        const val TJU_BIND: Int = 0xfaee01
        const val LIBRARY_BIND: Int = 0xaade3c
        const val TYPE: String = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<ImageView>(R.id.iv_activity_setting_back).setOnClickListener { onBackPressed() }

        val fragmentTransaction = fragmentManager.beginTransaction()
        val type = intent.getIntExtra(TYPE, 0)
        val message: String? = intent.getStringExtra("message")
        message?.let {
            Toasty.info(this, message).show()
        }
        if (type == TJU_BIND) {
            fragmentTransaction.add(R.id.settings_container, TjuBindFragment2())
        } else if (type == LIBRARY_BIND) {
            fragmentTransaction.add(R.id.settings_container, LibBindFragment2())
        }
        fragmentTransaction.commit()
    }
}

class TjuBindFragment2 : Fragment() {

    private lateinit var numEdit: EditText
    private lateinit var passwordEdit: EditText
    lateinit var button: Button
    private lateinit var unbinder: Unbinder


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_tju_bind_slide, container, false).also {
            unbinder = ButterKnife.bind(this, it)
        }
        button = view.findViewById(R.id.btn_tju_bind)
        numEdit = view.findViewById(R.id.tju_num)
        passwordEdit = view.findViewById(R.id.tju_password)

        button.setOnClickListener { _ ->
            RealBindAndDropOutService
                    .bindTju(numEdit.text.toString(), passwordEdit.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate {
                        login(CommonPreferences.twtuname, CommonPreferences.password) {
                            when (it) {
                                is RefreshState.Success -> {
                                    authSelfLiveData.refresh(CacheIndicator.REMOTE)
                                }
                                is RefreshState.Failure -> {
                                    Toasty.error(activity, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                }
                            }
                        }
                    }
                    .subscribe(Action1 {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Toasty.success(this.context, "绑定成功", Toast.LENGTH_SHORT).show()
                            activity.finish()
                        }
                    }, RxErrorHandler())
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}

class LibBindFragment2 : Fragment() {

    private lateinit var libPasswordEdit: EditText
    private lateinit var button: Button
    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lib_bind_slide, container, false).also {
            unbinder = ButterKnife.bind(this, it)
        }
        libPasswordEdit = view.findViewById(R.id.lib_password)
        button = view.findViewById(R.id.btn_lib_bind)
        button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TjuLibProvider(context).bindLibrary({ integer ->
                    when (integer) {
                        -1 -> {
                            login(CommonPreferences.twtuname, CommonPreferences.password) {
                                when (it) {
                                    is RefreshState.Success -> {
                                        authSelfLiveData.refresh(CacheIndicator.REMOTE)
                                    }
                                    is RefreshState.Failure -> {
                                        Toasty.error(activity, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                    }
                                }
                            }
                            Toasty.success(this.context, "绑定成功", Toast.LENGTH_SHORT).show()
                            activity.finish()
                        }
                        50002 -> Toasty.error(this.context, "图书馆密码错误", Toast.LENGTH_SHORT).show()
                        else -> Toasty.error(this.context, "未知错误", Toast.LENGTH_SHORT).show()
                    }
                }, libPasswordEdit.text.toString().takeIf(String::isNotEmpty) ?: "000000")
            }
        }
        return view
    }
}
