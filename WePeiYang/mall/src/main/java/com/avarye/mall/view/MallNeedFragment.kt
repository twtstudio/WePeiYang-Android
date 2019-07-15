package com.avarye.mall.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avarye.mall.R
import kotlinx.android.synthetic.main.mall_fragment_latest_need.view.*

class MallNeedFragment : Fragment() {

    lateinit var add: MainComponent

    private var redo: ((page: Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mall_fragment_latest_need, container, false)

        add = addMainItem(view.need, this, redo)

        bindNeed()
        return view
    }

    fun setDo(redo: (page: Int) -> Unit) {
        this.redo = redo
    }

    fun bindNeed() {
        add.bindNeed()
    }

    fun get() = add
}