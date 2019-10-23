package com.twt.service.settings

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator.REMOTE
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.auth.api.login
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider
import es.dmoral.toasty.Toasty

/**
 * Created by retrox on 01/03/2017.
 */

class LibBindFragment : SlideFragment() {

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
            TjuLibProvider(context).bindLibrary({ integer ->
                when (integer) {
                    -1 -> {
                        /* 由于后台的接口问题， 每次绑定解绑操作都要重新拿token，干脆用login接口，增加用户体验*/
                        login (CommonPreferences.twtuname, CommonPreferences.password) {
                            when (it) {
                                is RefreshState.Success -> {
                                    authSelfLiveData.refresh(REMOTE)
                                }
                                is RefreshState.Failure -> {
                                    Toasty.error(context!!, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                }
                            }
                        }
                        this.context?.let { it1 -> Toasty.success(it1, "图书馆绑定完成，点击底部右侧对勾开始新旅程", Toast.LENGTH_SHORT).show() }
                    }
                    50003 -> this.context?.let { it1 -> Toasty.success(it1, "图书馆已绑定，点击底部右侧对勾开始新旅程", Toast.LENGTH_SHORT).show() }
                    50002 -> this.context?.let { it1 -> Toasty.success(it1, "图书馆密码错误", Toast.LENGTH_SHORT).show() }
                    else -> this.context?.let { it1 -> Toasty.success(it1, "未知错误", Toast.LENGTH_SHORT).show() }
                }
            }, libPasswordEdit.text.toString().takeIf(String::isNotEmpty) ?: "000000")
        }
        return view
    }

    override fun backgroundColor(): Int = R.color.white_color

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    override fun buttonsColor(): Int = R.color.intro_slide_buttons

    override fun canMoveFurther() = true

    override fun cantMoveFurtherErrorMessage() = "请绑定图书馆账号！"
}
