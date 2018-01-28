package com.twt.service.home.user

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.twt.service.BuildConfig
import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twt.service.settings.SettingsActivity
import com.twt.wepeiyang.commons.cache.CacheProvider
import com.twt.wepeiyang.commons.utils.CommonPrefUtil
import com.twt.wepeiyang.commons.view.RecyclerViewDivider
import com.twtstudio.retrox.auth.api.AuthProvider
import com.twtstudio.retrox.auth.ext.map
import com.twtstudio.retrox.auth.view.LoginActivity


/**
 * Created by retrox on 2016/12/12.
 */

class UserFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserAdapter(
                    listOf(
                            UserItem.AvatarItem(AuthProvider.authSelfLiveData.map {
                                UserItem.AvatarBean(it.avatar, it.twtuname, it.realname)
                            }, R.drawable.ic_avatar),
                            UserItem.InfoItem(R.drawable.lib_library, "图书馆", AuthProvider.authSelfLiveData.map {
                                if (it.accounts.lib) "已绑定" else "未绑定"
                            }),
                            UserItem.InfoItem(R.drawable.bike_bike_icon, "自行车", MutableLiveData<String>().apply {
                                value = if (CommonPrefUtil.getIsBindBike()) "已绑定" else "未绑定"
                            }),
                            UserItem.InfoItem(R.drawable.ic_tju_little_icon, "办公网", AuthProvider.authSelfLiveData.map {
                                if (it.accounts.tju) "已绑定" else "未绑定"
                            }),
                            UserItem.ActionItem(R.drawable.ic_settings, "设置") {
                                val intent = Intent(context, SettingsActivity::class.java)
                                context.startActivity(intent)
                            },
                            UserItem.ActionItem(R.drawable.ic_settings_exit, "退出登录") {
                                AlertDialog.Builder(context)
                                        .setMessage("是否要登出？")
                                        .setPositiveButton("确定") { _, _ ->
                                            PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
                                            CommonPrefUtil.clearAll()

                                            // TODO: 27/03/2017 最后清理下缓存 debug不清理
                                            if (!BuildConfig.DEBUG) {
                                                CacheProvider.clearCache()
                                            }
                                            val intent = Intent(context, LoginActivity::class.java)

                                            //清除缓存
                                            context.startActivity(intent)
                                            (context as Activity).finish()
                                        }
                                        .setNegativeButton("取消") { _, _ ->
                                            Toast.makeText(context, "真爱啊 TAT...", Toast.LENGTH_SHORT).show()
                                        }.create().show()
                            }
                    ),
                    inflater,
                    this@UserFragment
            )

            addItemDecoration(RecyclerViewDivider.Builder(this.context)
                    .setStartSkipCount(1)
                    .setSize(2f)
                    .setColorRes(R.color.background_gray)
                    .build())
        }

        AuthProvider.authSelf(useCache = true, silent = true)
        return view
    }

    companion object {

        fun newInstance(): UserFragment {

            val args = Bundle()

            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
