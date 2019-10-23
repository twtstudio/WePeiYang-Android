package com.twt.service.home.user

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.tencent.stat.StatMultiAccount
import com.tencent.stat.StatService
import com.twt.service.R
import com.twt.service.ecard.model.EcardPref
import com.twt.service.ecard.model.isBindECardBoolean
import com.twt.service.ecard.model.isBindECardLiveData
import com.twt.service.ecard.view.EcardLoginActivity
import com.twt.service.settings.RealBindAndDropOutService
import com.twt.service.settings.SettingsActivity
import com.twt.service.settings.SingleBindActivity
import com.twt.wepeiyang.commons.cache.CacheProvider
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.map
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.network.RxErrorHandler
import com.twt.wepeiyang.commons.view.RecyclerViewDivider
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.retrox.auth.api.login
import com.twtstudio.retrox.auth.view.LoginActivity
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


/**
 * Created by retrox on 2016/12/12.
 */

class UserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_user, container, false).apply {
                findViewById<ImageView>(R.id.iv_fragment_user_back).setOnClickListener {
                    activity?.onBackPressed()
                }

                findViewById<RecyclerView>(R.id.recyclerView).apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = UserAdapter(
                            listOf(
                                    UserItem.AvatarItem(authSelfLiveData.map {
                                        UserItem.AvatarBean(it.avatar, it.twtuname, it.realname)
                                    }, R.drawable.ic_avatar),
                                    UserItem.InfoItem(R.drawable.ic_tju_little_icon, "办公网", authSelfLiveData.map {
                                        if (it.accounts.tju) "已绑定" else "未绑定"
                                    }) {
                                        if (CommonPreferences.isBindTju) {
                                            val builder = android.support.v7.app.AlertDialog.Builder(context)
                                                    .setTitle("办公网解绑")
                                                    .setMessage("是否要解绑办公网")
                                                    .setPositiveButton("解绑") { _, _ ->
                                                        RealBindAndDropOutService
                                                                .unbindTju(CommonPreferences.twtuname)
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .doAfterTerminate {
                                                                    /* 由于后台的接口问题， 每次绑定解绑操作都要重新拿token，干脆用login接口做了假解绑，增加用户体验*/
                                                                    login(CommonPreferences.twtuname, CommonPreferences.password) {
                                                                        when (it) {
                                                                            is RefreshState.Success -> {
                                                                                authSelfLiveData.refresh(CacheIndicator.REMOTE)
                                                                            }
                                                                            is RefreshState.Failure -> {
                                                                                Toasty.error(context, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                                .subscribe(Action1 { Toasty.success(context, "解绑成功！请重新绑定办公网", Toast.LENGTH_SHORT).show() }, RxErrorHandler())
                                                    }
                                                    .setNegativeButton("再绑会...") { dialog, _ -> dialog.dismiss() }
                                            builder.create().show()
                                        } else {
                                            val intent = Intent(activity, SingleBindActivity::class.java)
                                            intent.putExtra(SingleBindActivity.TYPE, SingleBindActivity.TJU_BIND)
                                            context.startActivity(intent)
                                        }
                                    },

                                    UserItem.InfoItem(R.drawable.lib_library, "图书馆", authSelfLiveData.map {
                                        if (it.accounts.lib) "已绑定" else "未绑定"
                                    }) {
                                        if (CommonPreferences.isBindLibrary) {
                                            val builder = android.support.v7.app.AlertDialog.Builder(context)
                                                    .setTitle("图书馆解绑")
                                                    .setMessage("是否要解绑图书馆")
                                                    .setPositiveButton("解绑") { dialog, _ ->
                                                        TjuLibProvider(activity).unbindLibrary {
                                                            login(CommonPreferences.twtuname, CommonPreferences.password) {
                                                                when (it) {
                                                                    is RefreshState.Success -> {
                                                                        authSelfLiveData.refresh(CacheIndicator.REMOTE)
                                                                    }
                                                                    is RefreshState.Failure -> {
                                                                        Toasty.error(context, "发生错误 ${it.throwable.message}！${it.javaClass.name}").show()
                                                                    }
                                                                }
                                                            }
//                                                            refreshToken()
//                                                            authSelfLiveData.refresh(CacheIndicator.REMOTE)
                                                            Toasty.success(context, "解绑成功！请重新绑定图书馆", Toast.LENGTH_SHORT).show()
                                                            dialog.dismiss()
                                                        }
                                                    }
                                                    .setNegativeButton("再绑会...") { dialog, _ -> dialog.dismiss() }
                                            builder.create().show()
                                        } else {
                                            val intent = Intent(activity, SingleBindActivity::class.java)
                                            intent.putExtra(SingleBindActivity.TYPE, SingleBindActivity.LIBRARY_BIND)
                                            context.startActivity(intent)
                                        }
                                    },

                                    UserItem.InfoItem(R.drawable.ic_user_ecard, "校园卡", isBindECardLiveData.map {
                                        if (it) "已绑定" else "未绑定"
                                    }) {
                                        if (isBindECardBoolean) {
                                            val builder = android.support.v7.app.AlertDialog.Builder(context)
                                                    .setTitle("校园卡解绑")
                                                    .setMessage("是否要解绑校园卡")
                                                    .setPositiveButton("解绑") { dialog, _ ->
                                                        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                                            EcardPref.ecardUserName = "*"
                                                            EcardPref.ecardPassword = "*"
                                                            isBindECardBoolean = false
                                                            isBindECardLiveData.value = false
                                                        }
                                                        Toasty.success(context, "解绑成功", Toast.LENGTH_SHORT).show()
                                                        dialog.dismiss()
                                                    }.setNegativeButton("再绑会...") { dialog, _ -> dialog.dismiss() }
                                            builder.create().show()
                                        } else {
                                            val intent = Intent(activity, EcardLoginActivity::class.java).apply {
                                                putExtra("from", "UserFragment")
                                            }
                                            context.startActivity(intent)
                                        }
                                    },

                                    UserItem.InfoItem(R.drawable.bike_bike_icon, "自行车", MutableLiveData<String>().apply {
                                        value = if (CommonPreferences.isBindBike) "已绑定" else "未绑定"
                                    }) {
                                        activity?.let { Toasty.info(it, "自行车", Toast.LENGTH_SHORT).show() }
                                    },
                                    UserItem.ActionItem(R.drawable.ic_settings, "设置") {
                                        val intent = Intent(context, SettingsActivity::class.java)
                                        context.startActivity(intent)
                                    },
                                    UserItem.ActionItem(R.drawable.ic_settings_exit, "登出") {
                                        AlertDialog.Builder(context)
                                                .setMessage("真的要登出吗？")
                                                .setPositiveButton("真的") { _, _ ->
                                                    PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
                                                    CommonPreferences.clear()
                                                    CacheProvider.clearCache()
                                                    StatService.removeMultiAccount(context, StatMultiAccount.AccountType.CUSTOM)
                                                    val intent = Intent(context, LoginActivity::class.java)

                                                    context.startActivity(intent)
                                                    (context as Activity).finish()
                                                }
                                                .setNegativeButton("算了") { _, _ ->
                                                    Toast.makeText(context, "真爱啊 TAT...", Toast.LENGTH_SHORT).show()
                                                }.create().show()
                                    }
                            ),
                            inflater,
                            this@UserFragment
                    )

                    addItemDecoration(RecyclerViewDivider.Builder(this.context)
                            .setSize(2f)
                            .setColorRes(R.color.transparent)
                            .build())
                }
            }

}
