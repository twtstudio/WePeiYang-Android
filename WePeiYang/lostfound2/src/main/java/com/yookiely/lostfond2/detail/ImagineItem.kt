package com.yookiely.lostfond2.detail

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

class ImagineItem(val images: List<String>, val detailActivity: DetailActivity) : Item {

    override val controller: ItemController
        get() = ImagineItem

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf2_item_imagine, parent, false)
            return ImagineHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ImagineHolder
            item as ImagineItem

            val popupWindowView = LayoutInflater.from(item.detailActivity).inflate(R.layout.lf2_detail_popupwindow, null, false)
            val popupWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            holder.banner.apply {
                setImageLoader(GlideImagineLoader())
                setImages(item.images)

                setBannerStyle(BannerConfig.NUM_INDICATOR)
                isAutoPlay(false)
                // 点击出现大图
                setOnBannerListener { position ->
                    popupWindow.apply {
                        isFocusable = true

                        Glide.with(context)
                                .load(item.images[position])
                                .error(R.drawable.lf_detail_np)
                                .into(popupWindowView.findViewById(R.id.iv_detail_popupwindow))

                        isOutsideTouchable = true
                        popupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(item.detailActivity, R.color.white_color)))
                        bgAlpha(0.5f,item.detailActivity)
                        showAsDropDown(item.detailActivity.toolbar, Gravity.CENTER, 0, 0)
                        setOnDismissListener {
                            // popupWindow 隐藏时恢复屏幕正常透明度
                            bgAlpha(1f,item.detailActivity)
                        }
                    }
                    // 点击布局文件（也可以理解为点击大图）
                }
                start()
            }
        }
    }

    private class ImagineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val banner: Banner = itemView.findViewById(R.id.br_detail_banner)
    }
}

private fun bgAlpha(bgAlpha: Float,detailActivity: DetailActivity) {
    detailActivity.apply {
        val lp = window.attributes
        lp.alpha = bgAlpha // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }
}

fun MutableList<Item>.imagines(images: List<String>,detailActivity: DetailActivity) = add(ImagineItem(images,detailActivity))