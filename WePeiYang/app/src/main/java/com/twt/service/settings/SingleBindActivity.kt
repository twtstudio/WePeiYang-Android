package com.twt.service.settings

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.rickygao.gpa2.spider.utils.SpiderTjuApi


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
        }
//        else if (type == LIBRARY_BIND) {
//            fragmentTransaction.add(R.id.settings_container, LibBindFragment2())
//        }
        fragmentTransaction.commit()
    }
}

class TjuBindFragment2 : Fragment() {

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
            etCaptcha.requestFocus()
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
            if (userNumber.trim() == "" || password.trim() == "" || captcha.trim() == "") {
                Toasty.warning(activity, "以上三个空不能为空").show()
                it.isClickable = true
                it.alpha = 1f
            } else {
                GlobalScope.launch(Main) {

                    val login = withContext(IO) {
                        SpiderTjuApi.login(userNumber, password, captcha)
                    }
                    when (login) {
                        200 -> {
                            CommonPreferences.tjuuname = userNumber
                            CommonPreferences.tjupwd = password
                            CommonPreferences.tjulogin = true
                            Toasty.success(activity, "成功登录办公网").show()
                            activity.finish()
                        }
                        else -> {
                            Toasty.error(activity, "信息填写有误").show()
                            refreshCaptcha()
                            withContext(IO) {
                                SpiderTjuApi.prepare()

                            }
                            etCaptcha.text = SpannableStringBuilder("")
                        }
                    }
                    it.isClickable = true
                    it.alpha = 1f
                }
            }

//            RealBindAndDropOutService
//                    .bindTju(numEdit.text.toString(), passwordEdit.text.toString())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doAfterTerminate {
//                        login(CommonPreferences.twtuname, CommonPreferences.password) {
//                            when (it) {
//                                is RefreshState.Success -> {
//                                    authSelfLiveData.refresh(CacheIndicator.REMOTE)
//                                }
//                                is RefreshState.Failure -> {
//                                    Toasty.error(activity, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
//                                }
//                            }
//                        }
//                    }
//                    .subscribe(Action1 {
//                        CommonPreferences.tjuuname = numEdit.text.toString()
//                        CommonPreferences.tjupwd = passwordEdit.text.toString()
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            Toasty.success(this.context, "绑定成功", Toast.LENGTH_SHORT).show()
//                            activity.finish()
//                        }
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
}
//
//class LibBindFragment2 : Fragment() {
//
//    private lateinit var libPasswordEdit: EditText
//    private lateinit var button: Button
//    private lateinit var unbinder: Unbinder
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_lib_bind_slide, container, false).also {
//            unbinder = ButterKnife.bind(this, it)
//        }
//        libPasswordEdit = view.findViewById(R.id.lib_password)
//        button = view.findViewById(R.id.btn_lib_bind)
//        button.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                TjuLibProvider(context).bindLibrary({ integer ->
//                    when (integer) {
//                        -1 -> {
//                            login(CommonPreferences.twtuname, CommonPreferences.password) {
//                                when (it) {
//                                    is RefreshState.Success -> {
//                                        authSelfLiveData.refresh(CacheIndicator.REMOTE)
//                                    }
//                                    is RefreshState.Failure -> {
//                                        Toasty.error(activity, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
//                                    }
//                                }
//                            }
//                            Toasty.success(this.context, "绑定成功", Toast.LENGTH_SHORT).show()
//                            activity.finish()
//                        }
//                        50002 -> Toasty.error(this.context, "图书馆密码错误", Toast.LENGTH_SHORT).show()
//                        else -> Toasty.error(this.context, "未知错误", Toast.LENGTH_SHORT).show()
//                    }
//                }, libPasswordEdit.text.toString().takeIf(String::isNotEmpty) ?: "000000")
//            }
//        }
//        return view
//    }
//}
