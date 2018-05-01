package com.twt.service.home.news


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twt.service.home.news.Model.newsLiveData
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.cache.simpleCallback
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.support.v4.act

//import com.twt.service.home.news.Model.NewsLiveData

/**
 * Created by retrox on 2016/12/12.
 */
class NewsFragment : BaseFragment() {
    private lateinit var textView : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_news, container, false).apply {
                textView = findViewById(R.id.textView1)
                newsLiveData.bindNonNull(this@NewsFragment) {
                    textView.text = it.toString()
                }
            }
    companion object {
        fun newInstance(): NewsFragment = NewsFragment().apply { arguments = Bundle() }
    }
}
