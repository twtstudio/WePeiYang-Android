package com.avarye.mall.detail

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.avarye.mall.R
import com.avarye.mall.comment.CommentActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.detailLiveData
import com.avarye.mall.service.sellerLiveData
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import kotlinx.android.synthetic.main.mall_activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var id: String
    private var presenter=DetailPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_detail)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)

        id = intent.getStringExtra("id")
        presenter.getId(id)
        presenter.getDetail()
        presenter.getInfo()

        detailLiveData.bindNonNull(this){

            Glide.with(this)
                    .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${it.imgurl}")
                    .into(iv_mall_detail_goods)
            tv_detail_locate.text=MallManager.getCampus(it.campus)
            tv_detail_price.text=it.price
            time.text=it.ctime
            tv_detail_name.text=it.name
            deteail_goods_tv.text=it.gdesc

            exchange_locate_text.text=it.location
            seller_tv.text=MallManager.getStatus(it.state)//商品成色
            barter_tv.text=it.exchange
            dicker_tv.text=MallManager.getBargain(it.bargain.toString())

            seller_name.text=it.username


            /*用detail的话返回的数据类型都是加密的
            qq_tv.text=it.qq
            wechat_three.text=it.email
            phone_tv.text=it.phone*/

        }
        sellerLiveData.bindNonNull(this){
            qq_tv.text=it.qq
            wechat_three.text=it.email
            phone_tv.text=it.phone


        }

        goods_collect.setOnClickListener {

        }

        goods_comment.setOnClickListener {
            var intent=Intent(this,CommentActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

    }



}
