package com.twtstudio.service.dishesreviews.account.view


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.simpleCallback
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twtstudio.retrox.auth.api.authSelfLiveData
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.account.model.AccountProvider
import com.twtstudio.service.dishesreviews.base.LazyFragment
import com.twtstudio.service.dishesreviews.extensions.displayImage

/**
 * Created by zhangyulong on 18-3-16.
 */
class AccountFragment : LazyFragment() {
    private lateinit var imgAccount: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvArticle: TextView
    private lateinit var tvPraiseNo: TextView
    private lateinit var tvCollectNo: TextView
    private lateinit var imgInfoIcon: ImageView
    override fun getResId(): Int {
        return R.layout.dishes_reviews_fragment_account
    }

    override fun onRealViewLoaded(view: View) {
        imgAccount = view.findViewById(R.id.img_account)
        tvUserName = view.findViewById(R.id.info_tv_username)
        tvArticle = view.findViewById(R.id.info_tv_article)
        imgInfoIcon = view.findViewById(R.id.info_icon)
        tvCollectNo = view.findViewById(R.id.tv_collect_no)
        tvPraiseNo = view.findViewById(R.id.tv_praise_no)
        tvUserName.text = CommonPreferences.twtuname
        AccountProvider.getAccount(CommonPreferences.studentid).apply {
            refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE, callback = context!!.simpleCallback())
            bindNonNull(this@AccountFragment) {
                tvArticle.text = "评论${it.commentNumber}篇"
            }
        }
        authSelfLiveData.bindNonNull(this) {
            imgInfoIcon.displayImage(context, it.avatar, ImageView.ScaleType.CENTER_INSIDE, R.drawable.dishes_reviews_ic_avatar)
        }


    }

}