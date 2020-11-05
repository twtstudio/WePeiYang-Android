package com.twt.service.settings

import agency.tango.materialintroscreen.SlideFragment
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.rickygao.gpa2.spider.utils.SpiderTjuApi

/**
 * Created by retrox on 01/03/2017.
 */

class TjuBindFragment : SlideFragment() {


    private lateinit var numEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var etCaptcha: EditText
    private lateinit var imgCaptcha: ImageView
    lateinit var button: Button
    private lateinit var unbinder: Unbinder
    private var session: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_tju_bind_slide, container, false).also {
            unbinder = ButterKnife.bind(this, it)
        }
        button = view.findViewById(R.id.btn_tju_bind)
        numEdit = view.findViewById(R.id.tju_num)
        passwordEdit = view.findViewById(R.id.tju_password)
        etCaptcha = view.findViewById(R.id.tju_kaptcha)
        imgCaptcha = view.findViewById(R.id.iv_captcha)
        GlobalScope.launch {
            withContext(IO + QuietCoroutineExceptionHandler) {
                SpiderTjuApi.logout()
                SpiderTjuApi.prepare()
                session = SpiderTjuApi.getSession()
            }
            withContext(Main) {
                refreshCaptcha()
            }
        }
        if (CommonPreferences.tjulogin == false) {
            // 如果是登录过期，才会使用上次登录的账号密码。
            // 如果是已登录状态进行重登，默认切换账号，所以不填入
            numEdit.text = SpannableStringBuilder(CommonPreferences.tjuuname)
            passwordEdit.text = SpannableStringBuilder(CommonPreferences.tjupwd)
        }


        imgCaptcha.setOnClickListener {
            refreshCaptcha()
        }
        button.setOnClickListener {
            it.isClickable = false
            it.alpha = 0.5f
            val userNumber = numEdit.text.toString()
            val password = passwordEdit.text.toString()
            val captcha = etCaptcha.text.toString()

            GlobalScope.launch(Main) {

                val login = withContext(IO) {
                    SpiderTjuApi.login(userNumber, password, captcha)
                }
                when (login) {
                    200 -> {
                        CommonPreferences.tjuuname = userNumber
                        CommonPreferences.tjupwd = password
                        CommonPreferences.tjulogin = true
                        context?.let { context ->
                            Toasty.normal(context, "登录成功").show()

                        }
                    }
                    else -> {
                        refreshCaptcha()
                        withContext(IO) {
                            SpiderTjuApi.prepare()

                        }
                        etCaptcha.text = SpannableStringBuilder("")
                        context?.let { context ->
                            Toasty.error(context, "登录失败").show()
                        }
                    }
                }
                it.isClickable = true
                it.alpha = 1f
            }


//            RealBindAndDropOutService
//                    .bindTju(numEdit.text.toString(), passwordEdit.text.toString())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doAfterTerminate {
//                        /* 由于后台的接口问题， 每次绑定解绑操作都要重新拿token，干脆用login接口做了假解绑，增加用户体验*/
//                        login(CommonPreferences.twtuname, CommonPreferences.password) {
//                            when (it) {
//                                is RefreshState.Success -> {
//                                    authSelfLiveData.refresh(REMOTE)
//                                }
//                                is RefreshState.Failure -> {
//                                    Toasty.error(context!!, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
//                                }
//                            }
//                        }
//                    }
//                    .subscribe(Action1 {
//                        this.context?.let { it1 ->
//                            CommonPreferences.tjuuname = numEdit.text.toString()
//                            CommonPreferences.tjupwd = passwordEdit.text.toString()
//                            Toasty.success(it1, "绑定成功", Toast.LENGTH_SHORT).show() }
//                    }, RxErrorHandler())
        }
        return view
    }

    private fun refreshCaptcha() {
        val cookie = GlideUrl(SpiderTjuApi.CAPTCHA_URL, LazyHeaders.Builder().addHeader("Cookie", session).build())
        Glide.with(activity).load(cookie)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ecard_loading2).into(imgCaptcha)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    override fun canMoveFurther() = CommonPreferences.tjulogin == true

    override fun buttonsColor() = R.color.intro_slide_buttons

    override fun backgroundColor() = R.color.white_color

    override fun cantMoveFurtherErrorMessage() = "办公网尚未绑定"
}
