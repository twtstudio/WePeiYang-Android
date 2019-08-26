package com.avarye.mall.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.PopupWindow
import com.avarye.mall.R
import com.avarye.mall.post.PostActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.ViewModel
import com.avarye.mall.service.loginLiveData
import com.avarye.mall.service.mineLiveData
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_mine.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import kotlinx.android.synthetic.main.mall_pop_setting.view.*
import org.jetbrains.anko.contentView

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
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)

        //toolbar
        tb_main.apply {
            title = getString(R.string.mallStringMine)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        mineLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
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

            bind()
            Toasty.info(this@MineActivity, it.id).show()
        }
    }

    private fun dealNull(str: String): String {
        return if (str.isBlank()) {
            "null"
        } else {
            str
        }
    }

    @SuppressLint("InflateParams")
    private fun bind() {
        cv_mine_sale.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.W_SALE)
            startActivity(intent)
        }

        cv_mine_need.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.W_NEED)
            startActivity(intent)
        }

        cv_mine_fav.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.FAV)
            startActivity(intent)
        }

        cv_mine_post_sale.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.W_SALE)
            startActivity(intent)
        }

        cv_mine_post_need.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
                    .putExtra(MallManager.TYPE, MallManager.W_NEED)
            startActivity(intent)
        }

        cv_mine_setting.setOnClickListener {
            val popupWindowView: View = LayoutInflater.from(this).inflate(R.layout.mall_pop_setting, null, false)
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
                bgAlpha(0.2f)
                setOnDismissListener { bgAlpha(1f) }

            }
        }
    }

    private fun bgAlpha(bgAlpha: Float) {
        val lp = window.attributes
        lp.alpha = bgAlpha // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mall_menu_refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (loginLiveData.value == null) {
//            viewModel.login()
        }
        mineLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
        return true
    }

}
