package com.twt.service.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.tencent.bugly.crashreport.CrashReport
import com.twt.service.R
import com.twt.service.update.UpdateManager
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.CAppCompatActivity

/**
 * Created by retrox on 2017/2/21.
 */

class SettingsActivity : CAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(findViewById(R.id.toolbar))
        title = ""
        findViewById<ImageView>(R.id.iv_activity_setting_back).setOnClickListener { onBackPressed() }
        fragmentManager.beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.prefs)
            initPrefs()
        }

        override fun onResume() {
            super.onResume()
//            val isBindLib = findPreference(getString(R.string.pref_is_bind_lib))
        }

        private fun initPrefs() {


            /**
             * 绑定模块
             */

//            val libBindPref = findPreference(getString(R.string.pref_bind_settings))
//            libBindPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//                val builder = AlertDialog.Builder(activity)
//                        .setMessage("是否要跳转到绑定页面")
//                        .setPositiveButton("确定") { _, _ ->
//                            val intent = Intent(activity, BindActivity::class.java)
//                            activity.startActivity(intent)
//                        }
//                builder.create().show()
//                true
//            }
//
//            val isBindTju = findPreference(getString(R.string.pref_is_bind_tju))
//            isBindTju.setOnPreferenceClickListener {
//                val builder = AlertDialog.Builder(activity)
//                        .setTitle("办公网解绑")
//                        .setMessage("是否要解绑办公网")
//                        .setPositiveButton("解绑") { _, _ ->
//                            if (CommonPreferences.isBindTju) {
//                                RealBindAndDropOutService
//                                        .unbindTju(CommonPreferences.twtuname)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .doAfterTerminate {
//                                            /* 由于后台的接口问题， 每次绑定解绑操作都要重新拿token，干脆用login接口做了假解绑，增加用户体验*/
//                                            login(CommonPreferences.twtuname, CommonPreferences.password) {
//                                                when (it) {
//                                                    is RefreshState.Success -> {
//                                                        authSelfLiveData.refresh(REMOTE)
//                                                    }
//                                                    is RefreshState.Failure -> {
//                                                        Toasty.error(activity, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        .subscribe(Action1 { Toasty.success(activity, "解绑成功！请重新绑定办公网", Toast.LENGTH_SHORT).show() }, RxErrorHandler())
//                            } else {
//                                Toasty.warning(activity, "你没绑定解绑啥？？？？？\n点击上面按钮进入绑定页面", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                        .setNegativeButton("再绑会...") { dialog, _ -> dialog.dismiss() }
//                builder.create().show()
//                false
//            }
//
//            val isBindLib = findPreference(getString(R.string.pref_is_bind_lib))
//            isBindLib.setOnPreferenceClickListener {
//                val builder = AlertDialog.Builder(activity)
//                        .setTitle("图书馆解绑")
//                        .setMessage("是否要解绑图书馆")
//                        .setPositiveButton("解绑") { dialog, _ ->
//                            if (CommonPreferences.isBindLibrary) {
//                                TjuLibProvider(activity).unbindLibrary {
//                                    login(CommonPreferences.twtuname, CommonPreferences.password) {
//                                        when (it) {
//                                            is RefreshState.Success -> {
//                                                authSelfLiveData.refresh(CacheIndicator.REMOTE)
//                                            }
//                                            is RefreshState.Failure -> {
//                                                Toasty.warning(activity, "你没绑定解绑啥？？？？？\n点击上面按钮进入绑定页面", Toast.LENGTH_SHORT).show()                                            }
//                                        }
//                                    }
//                                    Toasty.success(activity, "解绑成功！请重新绑定图书馆", Toast.LENGTH_SHORT).show()
//                                    dialog.dismiss()
//                                }
//                            } else {
//                                Toast.makeText(activity, "你没绑定解绑啥？？？？？\n点击上面按钮进入绑定页面", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                        .setNegativeButton("再绑会...") { dialog, _ -> dialog.dismiss() }
//                builder.create().show()
//                false
//            }
//
//            val isBindBike = findPreference(getString(R.string.pref_is_bind_bike))
////            isBindBike.summary = if (CommonPreferences.isBindBike) "已绑定" else "未绑定"
//            isBindBike.setOnPreferenceClickListener {
//                val builder = AlertDialog.Builder(activity)
//                        .setTitle("自行车解绑")
//                        .setMessage("是否要解绑自行车")
//                        .setPositiveButton("解绑") { _, _ ->
//                            if (CommonPreferences.isBindBike) {
//                                BikeServiceProvider(activity).unbind()
//                            } else {
//                                Toasty.warning(activity, "你没绑定解绑啥？？？？？\n进入自行车模块完成绑定", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                        .setNegativeButton("再绑会...") { dialog, _ -> dialog.dismiss() }
//                builder.create().show()
//                false
//            }
//
            val isDisplayBike = findPreference(getString(R.string.pref_is_display_bike))
            isDisplayBike.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                if (newValue == true) Toast.makeText(activity, "打开自行车模块以完成自行车功能的激活", Toast.LENGTH_SHORT).show()
                true
            }

            val devTalking = findPreference(getString(R.string.pref_dev_talking))
            devTalking.setOnPreferenceClickListener {
                val intent = Intent(activity, DevTalkActivity::class.java)
                activity.startActivity(intent)
                false
            }

            val feedback = findPreference(getString(R.string.pref_feedback))
            feedback.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://support.twtstudio.com/category/6/%E7%A7%BB%E5%8A%A8%E5%AE%A2%E6%88%B7%E7%AB%AF")
                activity.startActivity(intent)
                false
            }

            val contact = findPreference(getString(R.string.pref_contact_me))
            contact.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                try {
                    val email = "mobile@twtstudio.com"
                    val uri = Uri.parse("mailto:$email")
                    val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
                    activity.startActivity(emailIntent)
                } catch (e: Exception) {
                    Toasty.error(activity, "无法启动邮件发送APP", Toast.LENGTH_SHORT).show()
                    CrashReport.postCatchedException(e)
                    e.printStackTrace()
                }

                false
            }

            val checkUpdate = findPreference(getString(R.string.pref_check_update))
            checkUpdate.setOnPreferenceClickListener {
                UpdateManager.getInstance().checkUpdate(activity)
                true
            }

            val isAutoCheckUpdate = findPreference(getString(R.string.pref_is_auto_check_update)) as SwitchPreference
            isAutoCheckUpdate.isChecked = UpdateManager.getInstance().isAutoCheck
            isAutoCheckUpdate.setOnPreferenceChangeListener { _, newValue ->
                UpdateManager.getInstance().isAutoCheck = newValue as Boolean
                true
            }

            val addGroup = findPreference(getString(R.string.pref_add_group))
            addGroup.setOnPreferenceClickListener {
                joinQQGroup("DqYP6KYECBOV36yk1_RBE6iVjXbKVv0R")
            }
        }

        private fun processExitTju() {
            // TODO: 23/03/2017 退学逻辑
        }

        /****************
         *
         * 发起添加群流程。群号：天外天用户社区(738068756) 的 key 为： DqYP6KYECBOV36yk1_RBE6iVjXbKVv0R
         * 调用 joinQQGroup(DqYP6KYECBOV36yk1_RBE6iVjXbKVv0R) 即可发起手Q客户端申请加群 天外天用户社区(738068756)
         *
         * @param key 由官网生成的key
         * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
         */
        private fun joinQQGroup(key: String): Boolean {
            val intent = Intent()
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return try {
                startActivity(intent)
                true
            } catch (e: Exception) {
                // 未安装手Q或安装的版本不支持
                Toasty.error(activity, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show()
                false
            }

        }

    }


}
