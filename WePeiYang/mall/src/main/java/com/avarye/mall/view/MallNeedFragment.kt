package com.avarye.mall.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avarye.mall.R
import com.avarye.mall.service.LatestNeed

class MallNeedFragment : Fragment() {

    lateinit var add: MainComponent

    private var redo: ((page: Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mall_fragment_latest_need, container, false)

        add = addMainItem(view, redo)

        return view
    }

    fun setDo(redo: (page: Int) -> Unit) {
        this.redo = redo
    }

    fun bindNeed(data: List<LatestNeed>) {
        add.bindNeed(data)
    }

    fun get() = add
}