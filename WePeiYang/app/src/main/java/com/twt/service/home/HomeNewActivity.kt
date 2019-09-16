package com.twt.service.home

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.igexin.sdk.PushManager
import com.twt.service.AppPreferences
import com.twt.service.R
import com.twt.service.ecard.model.LiveEcardManager
import com.twt.service.ecard.view.ecardInfoItem
import com.twt.service.ecard.view.ecardTransactionInfoItem
import com.twt.service.home.message.*
import com.twt.service.home.other.homeOthers
import com.twt.service.home.user.FragmentActivity
import com.twt.service.schedule2.view.home.homeScheduleItem
import com.twt.service.tjunet.view.homeTjuNetItem
import com.twt.service.widget.ScheduleWidgetProvider
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twt.wepeiyang.commons.view.RecyclerViewDivider
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.tjulibrary.home.libraryHomeItem
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import pub.devrel.easypermissions.EasyPermissions
import xyz.rickygao.gpa2.view.gpaNewHomeItem


class HomeNewActivity : AppCompatActivity() {
    lateinit var rec: RecyclerView
    val messageIntent = Intent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EasyPermissions.requestPermissions(this, "微北洋需要外部存储来提供必要的缓存\n 需要位置信息和手机状态来获取校园网连接状态", 0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)

        setContentView(R.layout.activity_home_new)
        enableLightStatusBarMode(true)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorCompat(R.color.white)
        }
        val imageClickIntent = Intent(this@HomeNewActivity, ScheduleWidgetProvider::class.java).apply {
            action = "com.twt.appwidget.refresh"
        }
        sendBroadcast(imageClickIntent)
        val imageView = findViewById<ImageView>(R.id.iv_toolbar_avatar).apply {
            setOnClickListener {
                startActivity<FragmentActivity>("frag" to "User")
            }
        }
        authSelfLiveData.bindNonNull(this) {
            Glide.with(this).load(it.avatar).into(imageView)
        }
        rec = findViewById(R.id.rec_main)
        rec.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(RecyclerViewDivider.Builder(this@HomeNewActivity).setSize(4f).setColor(Color.TRANSPARENT).build())
            setItemViewCacheSize(10)
        }
        //注册个推
        //如果调用了registerPushIntentService方法注册自定义IntentService，则SDK仅通过IntentService回调推送服务事件；
        //如果未调用registerPushIntentService方法进行注册，则原有的广播接收器仍然可以继续使用。
        PushManager.getInstance().turnOnPush(this)
        PushManager.getInstance().initialize(this, MessagePushService::class.java)
        PushManager.getInstance().registerPushIntentService(this, MessageIntentService::class.java)
        messageIntent.setPackage(packageName)
        messageIntent.action = "com.twt.service.home.message.PUSH_SERVICE"
        startService(messageIntent)
        postRegister(CommonPreferences.twtuname,CommonPreferences.realName,CommonPreferences.studentid,MessagePreferences.gtclietid,1,0,0){
            Log.d("getui_register",it)
            Toasty.info(this,"it")
        }
        val itemManager = rec.withItems {
            // 重写了各个 item 的 areItemsTheSame areContentsTheSame 实现动画刷新主页
            if (MessagePreferences.isDisplayMessage) {
                homeMessageItem()
                mtaExpose("app_首页显示messageItem")
            }
            homeScheduleItem(this@HomeNewActivity)
            if (AppPreferences.isDisplayGpa) {
                gpaNewHomeItem(this@HomeNewActivity)
                mtaExpose("app_首页显示gpaItem")
            }
//            examTableHomeItem()
            ecardInfoItem()
            ecardTransactionInfoItem()
            libraryHomeItem(this@HomeNewActivity)
            homeTjuNetItem(this@HomeNewActivity)
            homeOthers()
        }
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val messageBean = MessageService.getMessage().await()
            val data = messageBean.data
            Log.d("HomeNew-Message-1", data.toString())
            if (messageBean.error_code == -1 && data != null) {
                // 通过新的网请和本地的 isDisplayMessage 判断来实现 Message 是否显示
                if (!MessagePreferences.isDisplayMessage && MessagePreferences.messageVersion != data.version) {
                    MessagePreferences.apply {
                        isDisplayMessage = true
                        messageTitle = data.title
                        messageContent = data.message
                        messageVersion = data.version
                    }
                    itemManager.homeMessageItemAtFirst()
                    rec.scrollToPosition(0)
                }
            }
        }
        rec.post {
            (rec.getChildAt(0)?.layoutParams as? RecyclerView.LayoutParams)?.topMargin = dip(4)
        }
    }

    override fun onResume() {
        super.onResume()
        PushManager.getInstance().initialize(this, MessagePushService::class.java)
        PushManager.getInstance().registerPushIntentService(this, MessageIntentService::class.java)
        LiveEcardManager.refreshEcardFullInfo()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rec.scrollToPosition(0)
        }
    }
}
