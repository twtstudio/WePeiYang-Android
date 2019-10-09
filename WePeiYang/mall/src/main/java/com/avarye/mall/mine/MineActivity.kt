package com.avarye.mall.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.avarye.mall.R
import com.avarye.mall.post.PostActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.MallManager.bgAlpha
import com.avarye.mall.service.MallManager.dealNull
import com.avarye.mall.service.ViewModel
import com.avarye.mall.service.mineLiveData
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import kotlinx.android.synthetic.main.mall_activity_mine.*
import kotlinx.android.synthetic.main.mall_popup_setting.view.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.withAlpha

/**
 * 个人主页
 */
class MineActivity : AppCompatActivity() {
    private var level = ""
    private var numb = ""
    private var token = ""
    private var phone = ""
    private var qq = ""
    private var email = ""
    private var campus = MallManager.WJL
    private val viewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_mine)
        window.statusBarColor = Color.TRANSPARENT.withAlpha(60)
        cv_mine_back.setOnClickListener { onBackPressed() }
        cv_mine_refresh.setOnClickListener { refresh() }

        mineLiveData.refresh(CacheIndicator.REMOTE)
        bindMine()
        bindButtons()
    }

    private fun bindMine() {
        mineLiveData.bindNonNull(this) {
            Glide.with(this@MineActivity)
                    .load(it.avatar)
                    .into(iv_mine_avatar)
            tv_mine_name.text = dealNull(it.nicheng)
            tv_mine_phone.text = dealNull(it.phone)
            tv_mine_qq.text = dealNull(it.qq)
            tv_mine_email.text = dealNull(it.email)
            tv_mine_campus.text = MallManager.getCampus(it.xiaoqu)
            level = it.level
            numb = it.numb
            token = it.token
            phone = it.phone
            qq = it.qq
            email = it.email
            campus = it.xiaoqu.toInt()
        }
    }

    @SuppressLint("InflateParams")
    private fun bindButtons() {
        cv_mine_sale.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.SALE)
            startActivity(intent)
        }

        cv_mine_need.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.NEED)
            startActivity(intent)
        }

        cv_mine_fav.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.FAV)
            startActivity(intent)
        }

        cv_mine_post_sale.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.SALE)
            startActivity(intent)
        }

        cv_mine_post_need.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.NEED)
            startActivity(intent)
        }

        cv_mine_setting.setOnClickListener {
            val popupWindowView: View = LayoutInflater.from(this).inflate(R.layout.mall_popup_setting, null, false)
            val popWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popWindow.apply {
                contentView.apply {
                    et_setting_phone.setText(phone)
                    et_setting_qq.setText(qq)
                    et_setting_email.setText(email)
                    when (campus) {
                        MallManager.WJL -> rg_setting_campus.check(R.id.rb_setting_campus1)
                        MallManager.BYY -> rg_setting_campus.check(R.id.rb_setting_campus2)
                    }

                    bt_setting_cancel.setOnClickListener { dismiss() }
                    bt_setting_commit.setOnClickListener {
                        phone = et_setting_phone.text.toString()
                        qq = et_setting_qq.text.toString()
                        email = et_setting_email.text.toString()
                        campus = when (rg_setting_campus.checkedRadioButtonId) {
                            rb_setting_campus1.id -> MallManager.WJL
                            rb_setting_campus2.id -> MallManager.BYY
                            else -> campus
                        }
                        viewModel.changeMyInfo(phone = phone, qq = qq, email = email, campus = campus)
                        dismiss()
                    }
                }

                showAtLocation(this@MineActivity.contentView, Gravity.CENTER, 0, 0)
                isOutsideTouchable = true
                isTouchable = true
                isFocusable = true
                bgAlpha(0.2f, this@MineActivity)
                setOnDismissListener { bgAlpha(1f, this@MineActivity) }
            }
        }
    }

    private fun refresh() {
        viewModel.login()
    }
}
