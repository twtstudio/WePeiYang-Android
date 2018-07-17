package com.example.lostfond2.mylist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.example.lostfond2.detail.DetailActivity
import com.example.lostfond2.release.ReleaseActivity
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.example.lostfond2.service.Utils
import com.twt.wepeiyang.commons.experimental.network.CommonBody


class MylistTableAdapter : RecyclerView.Adapter<MylistTableAdapter.MylistViewHolder> {


    var mylistBean: MutableList<MyListDataOrSearchBean>
    var context: FragmentActivity?
    var lostOrFound: String
    var mylistView: MyListService.MyListView

    constructor(mylistBean: MutableList<MyListDataOrSearchBean>, context: FragmentActivity?, lostOrFound: String, mylistView: MyListService.MyListView) {
        this.mylistBean = mylistBean
        this.context = context
        this.lostOrFound = lostOrFound
        this.mylistView = mylistView
    }


    class MylistViewHolder : RecyclerView.ViewHolder {
        constructor(view: View) : super(view) {
            mylist_item_status = view.findViewById(R.id.mylist_item_status)
            mylist_item_title = view.findViewById(R.id.mylist_item_title)
            mylist_item_type = view.findViewById(R.id.mylist_item_type)
            mylist_item_time = view.findViewById(R.id.mylist_item_time)
            mylist_item_place = view.findViewById(R.id.mylist_item_place)
            mylist_item_back_blue = view.findViewById(R.id.mylist_item_back_blue)
            mylist_item_back_grey = view.findViewById(R.id.mylist_item_back_grey)
            mylist_item_pencil = view.findViewById(R.id.mylist_item_pencil)
            mylist_item_pic = view.findViewById(R.id.mylist_item_pic)
            mylist_item_pencil_touch = view.findViewById(R.id.mylist_item_pencil_touch)


        }

        var mylist_item_status: TextView
        var mylist_item_title: TextView
        var mylist_item_type: TextView
        var mylist_item_time: TextView
        var mylist_item_place: TextView
        var mylist_item_back_blue: ImageView
        var mylist_item_back_grey: ImageView
        var mylist_item_pencil: ImageView
        var mylist_item_pic: ImageView

        var mylist_item_pencil_touch: TextView
    }

    override fun onBindViewHolder(holder: MylistViewHolder, position: Int) {
        val (id, _, title, place, time, _, detail_type, isback, picture) = mylistBean.get(position)
        holder.mylist_item_title.text = title
        holder.mylist_item_type.text = Utils.getType(detail_type)
        holder.mylist_item_time.text = time
        holder.mylist_item_place.text = place
        Glide.with(context)
                .load(Utils.getPicUrl(picture))
                .asBitmap()
                .placeholder(R.drawable.lf_waterfall_nopic)
                .into(holder.mylist_item_pic)

        if (isback == 1) {
            holder.mylist_item_back_blue.visibility = View.VISIBLE
            holder.mylist_item_back_grey.visibility = View.GONE
            if (lostOrFound == "found") {
                holder.mylist_item_status.text = "已交还"
            } else {
                holder.mylist_item_status.text = "已找到"
            }
        } else {
            holder.mylist_item_back_blue.visibility = View.GONE
            holder.mylist_item_back_grey.visibility = View.VISIBLE
            if (lostOrFound == "found") {
                holder.mylist_item_status.text = "未交还"
            } else {
                holder.mylist_item_status.text = "未找到"
            }
        }
        holder.mylist_item_status.setOnClickListener({ view -> mylistView.turnStatus(id) })

        val intent = Intent()
        val bundle = Bundle()
        holder.itemView.setOnClickListener { view ->
            bundle.putInt("id", id)
            intent.putExtras(bundle)
            intent.setClass(context, DetailActivity::class.java)
            context?.startActivity(intent)
        }
        holder.mylist_item_pencil_touch.setOnClickListener { view ->
            if (lostOrFound == "lost") {
                bundle.putString("lostOrFound", "editLost")
            } else {
                bundle.putString("lostOrFound", "editFound")
            }
            bundle.putInt("id", id)
            bundle.putInt("type", detail_type)
            intent.putExtras(bundle)
            intent.setClass(context, ReleaseActivity::class.java)
            context?.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MylistViewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.lf2_item_mylist, parent, false)
        return MylistViewHolder(view)
    }

    override fun getItemCount(): Int = mylistBean.size


}

