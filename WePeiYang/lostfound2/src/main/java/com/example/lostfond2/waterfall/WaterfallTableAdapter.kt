package com.example.lostfond2.waterfall

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
import com.example.lostfond2.detail.DetailActivity
import com.example.lostfond2.service.Data
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.example.lostfond2.service.Utils
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.android.synthetic.main.activity_release.*
import kotlinx.android.synthetic.main.lf2_item_waterfall.view.*

class WaterfallTableAdapter(var waterFallBean: List<MyListDataOrSearchBean>?,
                            val context: Context,
                            var lostOrFound: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val waterFall_item_pic = itemView.findViewById<ImageView>(R.id.lost_pic)
        val waterFall_item_thing = itemView.findViewById<TextView>(R.id.thing_content)
        val waterFall_item_data = itemView.findViewById<TextView>(R.id.data_content)//data其实是date
        val waterFall_item_location = itemView.findViewById<TextView>(R.id.location_content)
        val waterFall_tiem_title = itemView.findViewById<TextView>(R.id.title_content)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf2_item_waterfall, parent, false)

        return WaterfallViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataBean = waterFallBean!![position]

        val viewHolder = holder as WaterfallViewHolder

        if (dataBean.picture == null) {
            Glide.with(context)
                    .load(Utils.getPicUrl("julao.jpg"))
                    .placeholder(R.drawable.lf_detail_np)
                    .into(viewHolder.waterFall_item_pic)
        } else {
            Glide.with(context)
                    .load(Utils.getPicUrl(dataBean.picture))
                    .asBitmap()
                    .placeholder(R.drawable.lf_waterfall_nopic)
                    .into(viewHolder.waterFall_item_pic)
        }


        viewHolder.waterFall_tiem_title.text = dataBean.title
        viewHolder.waterFall_item_location.text = dataBean.place
        viewHolder.waterFall_item_thing.text = dataBean.name
        viewHolder.waterFall_item_data.text = dataBean.time

        viewHolder.itemView.setOnClickListener { view -> startDetailActivity(dataBean.id) }
    }

    override fun getItemCount(): Int = waterFallBean?.size ?: 0

    private fun startDetailActivity(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("lostOrFound", lostOrFound)
        val intent = Intent()
        intent.putExtras(bundle)
        intent.setClass(context, DetailActivity::class.java!!)
        context.startActivity(intent)

    }

}