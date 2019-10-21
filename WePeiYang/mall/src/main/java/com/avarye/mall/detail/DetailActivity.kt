package com.avarye.mall.detail

//import com.avarye.mall.service.collectLiveData
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.avarye.mall.R
import com.avarye.mall.main.MallActivity
import com.avarye.mall.mine.MineActivity
import com.avarye.mall.service.*
import com.avarye.mall.service.MallManager.dealNull
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.mall_activity_detail.*
import org.jetbrains.anko.withAlpha
import java.util.regex.Pattern

class DetailActivity : AppCompatActivity() {

    private var loginFlag = false
    private var id = ""
    private var token = ""
    private var type = ""
    private var flag = ""
    private val viewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_detail)
        window.apply {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            statusBarColor = Color.BLACK.withAlpha(80)
        }

        id = intent.getStringExtra(MallManager.ID)
        type = intent.getStringExtra(MallManager.TYPE)
        flag = intent.getStringExtra(MallManager.FROM_FLAG) ?: ""
        loginLiveData.bindNonNull(this) {
            when (type) {
                MallManager.SALE -> viewModel.getSellerSale(id, it.token)//拿sellerInfo
                MallManager.NEED -> viewModel.getSellerNeed(id, it.token)//换个参数拿need类sellerInfo
            }
            token = it.token
            loginFlag = true
        }

        when (type) {
            MallManager.SALE -> {
                bn_detail_banner.visibility = View.VISIBLE
                tv_detail_expect.visibility = View.GONE
                tv_detail_status.visibility = View.VISIBLE
                tv_detail_statusT.visibility = View.VISIBLE
                tv_detail_bargain.visibility = View.VISIBLE
                tv_detail_bargainT.visibility = View.VISIBLE
                iv_detail_fav.visibility = View.VISIBLE
            }
            MallManager.NEED -> {
                tv_detail_expect.visibility = View.VISIBLE
                bn_detail_banner.visibility = View.GONE
                tv_detail_status.visibility = View.GONE
                tv_detail_statusT.visibility = View.GONE
                tv_detail_bargain.visibility = View.GONE
                tv_detail_bargainT.visibility = View.GONE
                iv_detail_fav.visibility = View.GONE
            }
            else -> Unit
        }
        cv_detail_back.setOnClickListener { onBackPressed() }

        //bind data
        when (type) {
            MallManager.SALE -> {
                viewModel.getDetail(id)//只是为了要sale的imgUrl
                bindSale()
                bindSeller()
            }
            MallManager.NEED -> {
                bindNeed()
                bindSeller()
            }
            else -> Unit
        }

        //TODO:comment
//        val dialog = BottomSheetDialogFragment().show(supportFragmentManager, "dialog")
//        val view = layoutInflater.inflate(R.layout.mall_dialog_comment, null)

    }

    private fun bindSeller() {
        sellerLiveData.bindNonNull(this) {
            tv_detail_qqT.text = dealNull(it.qq)
            tv_detail_emailT.text = dealNull(it.email)
            tv_detail_phoneT.text = dealNull(it.phone)
        }
    }

    private fun bindSale() {
        detailLiveData.bindNonNull(this) {
            val imageList = separate(it.imgurl)
            bn_detail_banner.apply {
                setImageLoader(GlideImageLoader())
                setImages(imageList)
                setBannerStyle(BannerConfig.NUM_INDICATOR)
                isAutoPlay(false)
                setOnBannerListener { position ->
                    val dialog = Dialog(this@DetailActivity, R.style.edit_AlertDialog_style)
                    dialog.apply {
                        setContentView(R.layout.mall_dialog_detail_img)
                        val imageView = findViewById<ImageView>(R.id.iv_detail_whole)
                        Glide.with(this@DetailActivity)
                                .load(imageList[position])
                                .fitCenter()
                                .into(imageView)
                        setCanceledOnTouchOutside(true)
                        imageView.setOnClickListener { dismiss() }
                        show()
                    }
                }
                start()
            }
            tv_detail_campus.text = MallManager.getCampus(dealNull(it.campus))
            tv_detail_price.text = dealNull(it.price)
            tv_detail_time.text = dealNull(it.ctime)
            tv_detail_name.text = dealNull(it.name)
            tv_detail_detail.text = dealNull(it.gdesc)
            tv_detail_locateT.text = dealNull(it.location)
            tv_detail_statusT.text = MallManager.getStatus(dealNull(it.state))
            tv_detail_exchangeT.text = dealNull(it.exchange)
            tv_detail_bargainT.text = MallManager.getBargain(dealNull(it.bargain))
            tv_detail_seller.text = dealNull(it.username)
            when (it.is_collected) {
                true -> {
                    iv_detail_faved.visibility = View.VISIBLE
                    iv_detail_fav.visibility = View.GONE
                }
                false -> {
                    iv_detail_faved.visibility = View.GONE
                    iv_detail_fav.visibility = View.VISIBLE
                }

            }
            if (loginFlag) {
                iv_detail_fav.setOnClickListener {
                    iv_detail_fav.isClickable = false
                    iv_detail_faved.isClickable = true
                    viewModel.fav(id, token, this@DetailActivity)
                }
                iv_detail_faved.setOnClickListener {
                    iv_detail_fav.isClickable = true
                    iv_detail_faved.isClickable = false
                    viewModel.deFav(id, token, this@DetailActivity)
                }
            }
        }
    }

    private fun bindNeed() {
        detailLiveData.bindNonNull(this) {
            tv_detail_campus.text = MallManager.getCampus(dealNull(it.campus))
            tv_detail_price.text = dealNull(it.price)
            tv_detail_time.text = dealNull(it.ctime)
            tv_detail_name.text = dealNull(it.name)
            tv_detail_detail.text = dealNull(it.gdesc)
            tv_detail_locateT.text = dealNull(it.location)
            tv_detail_statusT.text = MallManager.getStatus(dealNull(it.state))
            tv_detail_exchangeT.text = dealNull(it.exchange)
            tv_detail_bargainT.text = MallManager.getBargain(dealNull(it.bargain))
            tv_detail_seller.text = dealNull(it.username)
        }
    }

    /**
     * separate origin data to a list
     */
    private fun separate(origin: String?): List<String> {
        val list = mutableListOf<String>()
        origin ?: return list
        val pattern = Pattern.compile("\\d+")
        val matcher = pattern.matcher(origin)
        while (matcher.find()) {
            list.add("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${matcher.group()}")
        }
        return list
    }

    override fun onBackPressed() = when (flag) {
        MallManager.FROM_MALL -> {
            val intent = Intent(this, MallActivity::class.java)
            startActivity(intent)
        }
        MallManager.FROM_MINE -> {
            val intent = Intent(this, MineActivity::class.java)
            startActivity(intent)
        }
        else -> super.onBackPressed()
    }
}
