package com.twt.service.home.news


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.R
import com.twt.service.base.BaseFragment

/**
 * Created by retrox on 2016/12/12.
 */

class NewsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_news, container, false)

    companion object {
        fun newInstance(): NewsFragment = NewsFragment().apply { arguments = Bundle() }
    }
}
