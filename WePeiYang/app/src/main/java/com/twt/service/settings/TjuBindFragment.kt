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
import com.twt.wepeiyang.commons.network.RxErrorHandler
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.auth.api.login
import es.dmoral.toasty.Toasty
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * Created by retrox on 01/03/2017.
 */

class TjuBindFragment : SlideFragment() {


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
                        /* 由于后台的接口问题， 每次绑定解绑操作都要重新拿token，干脆用login接口做了假解绑，增加用户体验*/
                        login(CommonPreferences.twtuname, CommonPreferences.password) {
                            when (it) {
                                is RefreshState.Success -> {
                                    authSelfLiveData.refresh(REMOTE)
                                }
                                is RefreshState.Failure -> {
                                    Toasty.error(context!!, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                }
                            }
                        }
                    }
                    .subscribe(Action1 {
                        this.context?.let { it1 -> Toasty.success(it1, "绑定成功", Toast.LENGTH_SHORT).show() }
                    }, RxErrorHandler())
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    override fun canMoveFurther() = CommonPreferences.isBindTju

    override fun buttonsColor() = R.color.intro_slide_buttons

    override fun backgroundColor() = R.color.white_color

    override fun cantMoveFurtherErrorMessage() = "办公网尚未绑定"
}
