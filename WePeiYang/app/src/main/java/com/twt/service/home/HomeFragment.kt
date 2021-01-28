package com.twt.service.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.twt.service.AppPreferences
import com.twt.service.R
import com.twt.service.announcement.ui.annoBannerItem
import com.twt.service.announcement.ui.annoHomeItem
import com.twt.service.home.message.*
import com.twt.service.home.other.homeOthers
import com.twt.service.home.user.FragmentActivity
import com.twt.service.schedule2.view.home.homeScheduleItem
import com.twt.service.update.UpdateManager
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twt.wepeiyang.commons.view.RecyclerViewDivider
import com.twtstudio.retrox.auth.api.authSelfLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.dip
import xyz.rickygao.gpa2.view.gpaNewHomeItem


class HomeFragment: Fragment() {
    lateinit var rec: RecyclerView
    val messageIntent = Intent()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_home_new, container, false)
        val mContext = activity
//        val itemManager = ItemManager()
//        val imageView = view.findViewById<ImageView>(R.id.iv_toolbar_avatar).apply {
//            setOnClickListener {
//                mContext?.startActivity<FragmentActivity>("frag" to "User")
//            }
//        }

        authSelfLiveData.bindNonNull(this) {
//            Glide.with(this).load(it.avatar).into(imageView)
            if(it.telephone==null) {
                val intent = Intent(mContext, HomeFragment::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        rec = view.findViewById(R.id.rec_main)
        rec.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(RecyclerViewDivider.Builder(mContext).setSize(4f).setColor(Color.TRANSPARENT).build())
            setItemViewCacheSize(10)
        }

        UpdateManager.getInstance().checkUpdate(mContext, false)

        val itemManager = rec.withItems {
            // 重写了各个 item 的 areItemsTheSame areContentsTheSame 实现动画刷新主页
            if (MessagePreferences.isDisplayMessage) {
                homeMessageItem()
                mtaExpose("app_首页显示messageItem")
            }
            annoBannerItem()

            if (AppPreferences.isDisplaySchedule) {
                homeScheduleItem(this@HomeFragment)
                mtaExpose("app_首页显示scheduleItem")
            }

//            homeScheduleItem(this@HomeNewActivity)
            if (AppPreferences.isDisplayGpa) {
                gpaNewHomeItem(this@HomeFragment)
                mtaExpose("app_首页显示gpaItem")
            }

//            annoHomeItem()
//            examTableHomeItem()
//            ecardInfoItem()
//            ecardTransactionInfoItem()
//            libraryHomeItem(this@HomeNewActivity)
//            homeTjuNetItem(this@HomeNewActivity)
            homeOthers()
        }

        //TODO(这里后台数据返回的有问题，json无法解析)
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val messageBean = MessageService.getMessage().awaitAndHandle {
                Log.d("testjsoncanconvert", it.message.toString())
            }
            val data = messageBean?.data
            Log.d("HomeNew-Message-1", data.toString())
            if (messageBean?.error_code == -1 && data != null) {
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
        return view
    }
}