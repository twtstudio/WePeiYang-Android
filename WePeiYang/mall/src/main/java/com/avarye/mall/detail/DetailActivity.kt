package com.avarye.mall.detail

//import com.avarye.mall.service.collectLiveData
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.avarye.mall.R
import com.avarye.mall.service.*
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_detail.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*

class DetailActivity : AppCompatActivity() {

    var gid = ""
    var token = ""
    private val viewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_detail)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)

        tb_main.apply {
            title = getString(R.string.mallStringDetail)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        gid = intent.getStringExtra("id")
        loginLiveData.bindNonNull(this) {
            token = it.token
            Toasty.info(this, token).show()
        }

        detailLiveData.bindNonNull(this) {
            Glide.with(this)
                    .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${it.imgurl}")
                    .into(vp_detail)
            tv_detail_campus.text = MallManager.getCampus(it.campus)
            tv_detail_price.text = it.price
            tv_detail_time.text = it.ctime
            tv_detail_name.text = it.name
            tv_detail_detail.text = it.gdesc
            tv_detail_locateT.text = it.location
            tv_detail_statusT.text = MallManager.getStatus(it.state)
            tv_detail_exchangeT.text = dealNull(it.exchange)
            tv_detail_bargainT.text = MallManager.getBargain(it.bargain.toString())
            tv_detail_seller.text = it.username
        }

        sellerLiveData.bindNonNull(this) {
            tv_detail_qqT.text = dealNull(it.qq)
            tv_detail_emailT.text = dealNull(it.email)
            tv_detail_phoneT.text = dealNull(it.phone)
        }

        iv_detail_fav.setOnClickListener {
            viewModel.fav(gid, token)
        }

        iv_detail_comment.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("gid", gid)
            startActivity(intent)
        }

        viewModel.getDetail(gid, token)
    }

    private fun dealNull(str: String): String {
        return if (str.isBlank()) {
            "卖家暂未说明"
        } else {
            str
        }
    }
}
