package com.twtstudio.retrox.news.explore

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper
import com.twtstudio.retrox.news.R
import com.twtstudio.retrox.news.api.PicProvider
import me.yokeyword.fragmentation.SupportFragment
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * @deprecated
 * Created by retrox on 09/04/2017.
 * 从入门到放弃
 * 坑太多了
 */
class ExploreFragmentKt : SupportFragment() {

    val virtualLayoutManger by lazy { VirtualLayoutManager(this.activity) }
    val delegateAdapter = DelegateAdapter(virtualLayoutManger, true)
    val picApi = PicProvider().picApi


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        val recyclerview = view.findViewById(R.id.recyclerview) as RecyclerView
        recyclerview.recycledViewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 20) }
        recyclerview.layoutManager = virtualLayoutManger
        recyclerview.adapter = delegateAdapter

        // 主站图集的header
        delegateAdapter.addAdapter(SingleItem(activity,SingleLayoutHelper(),R.layout.item_explore_gallery_header))

        //主站图集的主体
        val galleryIndexAdapter = GalleryIndexAdapter(context, GridLayoutHelper(2).apply {
            setGap(8)
            setAutoExpand(true)
            bgColor = Color.WHITE
        })
        delegateAdapter.addAdapter(galleryIndexAdapter)
        picApi.getGalleryIndex().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({galleryIndexAdapter.refreshData(it)},Throwable::printStackTrace)

        // 方寸流年的header
        val gridHeader = SingleItem(activity, SingleLayoutHelper().apply {
            marginTop = 32
        })
        delegateAdapter.addAdapter(gridHeader)

        // 方寸流年的主体
        val gridHelper = GridLayoutHelper(3)
        gridHelper.bgColor = Color.WHITE
//        gridHelper.setSpanSizeLookup(spanLookUp())
        gridHelper.setAutoExpand(true)
        gridHelper.setGap(8)
        val vistaAdapter = VistaAdapter(activity, gridHelper)
        delegateAdapter.addAdapter(vistaAdapter)
        picApi.getFangcunPic().subscribeOn(Schedulers.io())
                .map { it.data }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Collections.shuffle(it,Random(System.currentTimeMillis()))
                    vistaAdapter.refreshData(it.subList(0, 5))
                }, Throwable::printStackTrace)

        // 方寸流年的footer
        val gridFooter = VistaSingleItemFooter(activity, SingleLayoutHelper())
        delegateAdapter.addAdapter(gridFooter)

        return view
    }

    //有坑!
    class spanLookUp : GridLayoutHelper.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            when (position) {
                in 0..2 -> return 2
                3 -> return 4
                4 -> return 2
                else -> return 2
            }
        }
    }

}