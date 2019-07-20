package com.avarye.mall.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avarye.mall.R
import com.avarye.mall.service.menuLiveData
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemManager

class MenuFragment : Fragment() {

    private val itemManager = ItemManager()


    companion object {
        fun newInstance() = MenuFragment()
    }

//    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mall_fragment_menu, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        menuLiveData.bindNonNull(this) {

            itemManager.autoRefresh {
//                removeAll { it is MenuItem }//介不用吧

                val items = mutableListOf<Item>().apply {
                    it.forEach {
                        addMenuItem {
                            name.text = it.name
                            name.setOnClickListener {
                            }
                        }
                        it.smalllist.forEach { sit->
                            addMenuItem {
                                name.text = sit.name
                            }
                        }
                    }
                }
                addAll(0, items)
            }

        }

    }

    fun bindSubList() {

    }
}
