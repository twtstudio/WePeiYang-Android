package com.avarye.mall.mine

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.avarye.mall.R
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.main.SaleItem
import com.avarye.mall.main.saleItem
import com.avarye.mall.service.*
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_list.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*

/**
 * 我的发布/求购/收藏列表
 */
class ListActivity : AppCompatActivity() {

    private var isLoading = false
    private val itemManager = ItemManager()
    private val viewModel = ViewModel()
    private var token = ""
    private var uid = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_list)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)
        iv_list_null.visibility = View.INVISIBLE

        loginLiveData.bindNonNull(this) {
            uid = it.uid
            token = it.token
            //发请求 bind
            post()
        }
        type = intent.getStringExtra(MallManager.TYPE)

        tb_main.apply {
            title = when (type) {
                MallManager.SALE -> getString(R.string.mallStringMySale)
                MallManager.NEED -> getString(R.string.mallStringMyNeed)
                MallManager.FAV -> getString(R.string.mallStringMyFav)
                else -> "薛定谔的页面"
            }
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        srl_list.apply {
            setColorSchemeResources(R.color.mallColorMain)
            //下拉刷新加载监听
            setOnRefreshListener { refresh() }
        }

        rv_list.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            itemManager.autoRefresh { removeAll { it is SaleItem } }
        }

        when (type) {
            MallManager.SALE -> bindSale()
            MallManager.NEED -> bindNeed()
            MallManager.FAV -> bindFav()
        }
    }

    fun post() = when (type) {
        MallManager.SALE -> viewModel.getMyList(uid, MallManager.W_SALE)
        MallManager.NEED -> viewModel.getMyList(uid, MallManager.W_NEED)
        MallManager.FAV -> viewModel.getFavList(token)
        else -> Unit
    }

    fun refresh() {
        if (!isLoading) {
            isLoading = true
            iv_list_null.visibility = View.INVISIBLE
            itemManager.autoRefresh { removeAll { it is SaleItem } }
            //redo
            post()
            srl_list.isRefreshing = false
            Toasty.success(this@ListActivity, "已刷新").show()
        }
        isLoading = false
    }

    private fun bindSale() {
        myListLiveData.bindNonNull(this) { list ->
            if (list.isEmpty()) {
                itemManager.autoRefresh { removeAll { it is SaleItem } }
                iv_list_null.visibility = View.VISIBLE
            } else {
                iv_list_null.visibility = View.INVISIBLE
                val items = mutableListOf<Item>().apply {
                    for (i in list.indices) {
                        saleItem {
                            Glide.with(this@ListActivity)
                                    .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                    .into(image)
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.apply {
                                setOnClickListener {
                                    val intent = Intent(this@ListActivity, DetailActivity::class.java)
                                            .putExtra(MallManager.ID, list[i].id)
                                            .putExtra(MallManager.TYPE, MallManager.SALE)
                                    startActivity(intent)
                                }
                                setOnLongClickListener {
                                    editDialog(list[i].id)
                                    true
                                }
                            }
                        }
                    }
                }
                itemManager.autoRefresh {
                    removeAll { it is SaleItem }
                    addAll(items)
                }
            }
        }
    }

    private fun bindNeed() {
        myListLiveData.bindNonNull(this) { list ->
            if (list.isEmpty()) {
                itemManager.autoRefresh { removeAll { it is SaleItem } }
                iv_list_null.visibility = View.VISIBLE
            } else {
                iv_list_null.visibility = View.INVISIBLE
                val items = mutableListOf<Item>().apply {
                    for (i in list.indices) {
                        saleItem {
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.apply {
                                setOnClickListener {
                                    detailLiveData.postValue(list[i])
                                    val intent = Intent(this@ListActivity, DetailActivity::class.java)
                                            .putExtra(MallManager.ID, list[i].id)
                                            .putExtra(MallManager.TYPE, MallManager.NEED)
                                    this@ListActivity.startActivity(intent)
                                }
                                setOnLongClickListener {
                                    editDialog(list[i].id)
                                    true
                                }
                            }

                        }
                    }
                }
                itemManager.autoRefresh {
                    removeAll { it is SaleItem }
                    addAll(items)
                }
            }
        }
    }

    private fun bindFav() {
        myListLiveData.bindNonNull(this) { list ->
            if (list.isEmpty()) {
                itemManager.autoRefresh { removeAll { it is SaleItem } }
                iv_list_null.visibility = View.VISIBLE
            } else {
                iv_list_null.visibility = View.INVISIBLE
                val items = mutableListOf<Item>().apply {
                    for (i in list.indices) {
                        saleItem {
                            Glide.with(this@ListActivity)
                                    .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                    .into(image)
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.apply {
                                setOnClickListener {
                                    val intent = Intent(this@ListActivity, DetailActivity::class.java)
                                            .putExtra(MallManager.ID, list[i].id)
                                            .putExtra(MallManager.TYPE, MallManager.SALE)
                                    this@ListActivity.startActivity(intent)
                                }
                                setOnLongClickListener {
                                    editDialog(list[i].id)
                                    true
                                }
                            }
                        }
                    }
                }
                itemManager.autoRefresh {
                    removeAll { it is SaleItem }
                    addAll(items)
                }
            }
        }
    }

    private fun editDialog(id: String) {
        val dialog = AlertDialog.Builder(this)
                .setMessage(when (type) {
                    MallManager.SALE -> getString(R.string.mallStringDeleteSale)
                    MallManager.NEED -> getString(R.string.mallStringDeleteNeed)
                    MallManager.FAV -> getString(R.string.mallStringDeFav)
                    else -> "薛定谔又来了"
                })
                .setCancelable(false)
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("确定") { dialog, _ ->
                    run {
                        when (type) {
                            MallManager.SALE -> {
                                viewModel.deleteSale(id, token, uid)
                            }
                            MallManager.NEED -> {
                                viewModel.deleteNeed(id, token, uid)
                            }
                            MallManager.FAV -> {
                                viewModel.deFav(id, token)
                            }
                            else -> Unit
                        }
                        dialog.dismiss()
                    }
                }
                .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.mallColorMain))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.mallColorPressed))
    }
}
