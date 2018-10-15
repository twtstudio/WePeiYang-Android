package com.yookiely.lostfond2.waterfall

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.yookiely.lostfond2.detail.DetailActivity
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils

class WaterfallTableAdapter(var waterFallBean: List<MyListDataOrSearchBean>?,
                            val context: Context,
                            var lostOrFound: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val waterfall_item_pic = itemView.findViewById<ImageView>(R.id.lost_pic)
        val waterfall_item_thing = itemView.findViewById<TextView>(R.id.thing_content)
        val waterfall_item_data = itemView.findViewById<TextView>(R.id.data_content)//data其实是date
        val waterfall_item_location = itemView.findViewById<TextView>(R.id.location_content)
        val waterfall_item_title = itemView.findViewById<TextView>(R.id.title_content)
        val waterfall_item_recapture_place = itemView.findViewById<TextView>(R.id.recapture_place)
        val waterfall_item_recapture = itemView.findViewById<ImageView>(R.id.recapture)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf2_item_waterfall, parent, false)

        return WaterfallViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataBean = waterFallBean!![position]
        val viewHolder = holder as WaterfallViewHolder

        if (dataBean.picture == null) {
            Glide.with(context)
                    .load(Utils.getPicUrl("julao.jpg"))
                    .placeholder(R.drawable.lf_detail_np)
                    .into(viewHolder.waterfall_item_pic)
        } else {
            val piclist: List<String> = dataBean.picture.split(",")
            Glide.with(context)
                    .load(Utils.getPicUrl(piclist[0]))
                    .asBitmap()
                    .placeholder(R.drawable.lf_waterfall_nopic)
                    .into(viewHolder.waterfall_item_pic)
        }

        viewHolder.apply {
            waterfall_item_title.text = dataBean.title
            waterfall_item_location.text = dataBean.place
            waterfall_item_thing.text = dataBean.name
            waterfall_item_data.text = dataBean.time

            if (lostOrFound == "found") {
                waterfall_item_recapture_place.text = dataBean.recapture_place
            } else {
                waterfall_item_recapture.visibility = View.GONE
                waterfall_item_recapture_place.visibility = View.GONE
            }
        }
        viewHolder.itemView.setOnClickListener { view -> startDetailActivity(dataBean.id) }
    }

    override fun getItemCount(): Int = waterFallBean?.size ?: 0

    private fun startDetailActivity(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("lostOrFound", lostOrFound)
        val intent = Intent()
        intent.putExtras(bundle)
        intent.setClass(context, DetailActivity::class.java)
        context.startActivity(intent)
    }

}