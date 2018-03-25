package com.twt.service.settings

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator.REMOTE
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.network.RxErrorHandler
import com.twtstudio.retrox.auth.api.authSelfLiveData
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * Created by retrox on 01/03/2017.
 */

class TjuBindFragment : SlideFragment() {

    @BindView(R.id.tju_num)
    private lateinit var numEdit: EditText
    @BindView(R.id.tju_password)
    private lateinit var passwordEdit: EditText
    private lateinit var unbinder: Unbinder

    @OnClick(R.id.btn_tju_bind)
    fun bind(view: View) {
        RealBindAndDropOutService
                .bindTju(numEdit.text.toString(), passwordEdit.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { authSelfLiveData.refresh(REMOTE) }
                .subscribe(Action1 {
                    Toast.makeText(this.context, "绑定成功", Toast.LENGTH_SHORT).show()
                }, RxErrorHandler())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_tju_bind_slide, container, false).also {
                unbinder = ButterKnife.bind(this, it)
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
