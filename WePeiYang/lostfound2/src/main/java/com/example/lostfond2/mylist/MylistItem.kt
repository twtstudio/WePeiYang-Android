package com.example.lostfond2.mylist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
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
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater


class MylistItem(val context: FragmentActivity?, val lostOrFound: String, val mylistBean: MyListDataOrSearchBean, val mylistView: MyListService.MyListView) : Item {


    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf2_item_mylist, parent, false)
            return MylistItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {

            holder as MylistItemViewHolder
            item as MylistItem



            val (id, _, title, place, time, _, detail_type, isback, picture) = item.mylistBean
            holder.mylist_item_title.text = title
            holder.mylist_item_type.text = Utils.getType(detail_type)
            holder.mylist_item_time.text = time
            holder.mylist_item_place.text = place

            Glide.with(item.context)
                    .load(Utils.getPicUrl(picture))
                    .asBitmap()
                    .placeholder(R.drawable.lf_waterfall_nopic)
                    .into(holder.mylist_item_pic)

            if (isback == 1) {
                holder.mylist_item_back_blue.visibility = View.VISIBLE
                holder.mylist_item_back_grey.visibility = View.GONE
                if (item.lostOrFound == "found") {
                    holder.mylist_item_status.text = "已交还"
                } else {
                    holder.mylist_item_status.text = "已找到"
                }
            } else {
                holder.mylist_item_back_blue.visibility = View.GONE
                holder.mylist_item_back_grey.visibility = View.VISIBLE
                if (item.lostOrFound == "found") {
                    holder.mylist_item_status.text = "未交还"
                } else {
                    holder.mylist_item_status.text = "未找到"
                }
            }
            holder.mylist_item_status.setOnClickListener({ view -> item.mylistView.turnStatus(id) })

            val intent = Intent()
            val bundle = Bundle()
            holder.itemView.setOnClickListener { view ->
                bundle.putInt("id", id)
                intent.putExtras(bundle)
                intent.setClass(item.context, DetailActivity::class.java)
                item.context?.startActivity(intent)
            }
            holder.mylist_item_pencil_touch.setOnClickListener { view ->
                if (item.lostOrFound == "lost") {
                    bundle.putString("lostOrFound", "editLost")
                } else {
                    bundle.putString("lostOrFound", "editFound")
                }
                bundle.putInt("id", id)
                bundle.putInt("type", detail_type)
                intent.putExtras(bundle)
                intent.setClass(item.context, ReleaseActivity::class.java)
                item.context?.startActivity(intent)
            }
        }

    }


    private class MylistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mylist_item_status: TextView = itemView.findViewById(R.id.mylist_item_status)
        val mylist_item_title: TextView = itemView.findViewById(R.id.mylist_item_title)
        val mylist_item_type: TextView = itemView.findViewById(R.id.mylist_item_type)
        val mylist_item_time: TextView = itemView.findViewById(R.id.mylist_item_time)
        val mylist_item_place: TextView = itemView.findViewById(R.id.mylist_item_place)
        val mylist_item_back_blue: ImageView = itemView.findViewById(R.id.mylist_item_back_blue)
        val mylist_item_back_grey: ImageView = itemView.findViewById(R.id.mylist_item_back_grey)
        val mylist_item_pencil: ImageView = itemView.findViewById(R.id.mylist_item_pencil)
        val mylist_item_pic: ImageView = itemView.findViewById(R.id.mylist_item_pic)
        val mylist_item_pencil_touch: TextView = itemView.findViewById(R.id.mylist_item_pencil_touch)

    }


    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.mylistload(context: FragmentActivity?, lostOrFound: String, mylistBean: MyListDataOrSearchBean, mylistView: MyListService.MyListView) = add(MylistItem(context, lostOrFound, mylistBean, mylistView))