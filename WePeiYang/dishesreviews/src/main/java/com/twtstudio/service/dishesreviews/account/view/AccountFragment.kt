package com.twtstudio.service.dishesreviews.account.view


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.account.model.AccountProvider
import com.twtstudio.service.dishesreviews.base.LazyFragment

/**
 * Created by zhangyulong on 18-3-16.
 */
class AccountFragment : LazyFragment() {
    private lateinit var imgAccount: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvArticle: TextView
    private lateinit var imgInfoIcon: ImageView
    override fun getResId(): Int {
        return R.layout.dishes_reviews_fragment_account
    }

    override fun onRealViewLoaded(view: View) {
        imgAccount = view.findViewById<ImageView>(R.id.img_account)
        tvUserName = view.findViewById<TextView>(R.id.info_tv_username)
        tvArticle = view.findViewById<TextView>(R.id.info_tv_article)
        imgInfoIcon = view.findViewById<ImageView>(R.id.info_icon)
        tvUserName.text = CommonPreferences.twtuname
        AccountProvider.getAccount(CommonPreferences.studentid).bindNonNull(this) {
            tvArticle.text = "评论${it.commentNumber}篇"
        }
    }

}