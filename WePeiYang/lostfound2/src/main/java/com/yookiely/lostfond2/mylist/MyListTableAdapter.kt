package com.yookiely.lostfond2.mylist

import android.annotation.SuppressLint
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
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.detail.DetailActivity
import com.yookiely.lostfond2.release.ReleaseActivity
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils

class MyListTableAdapter(var myListBean: MutableList<MyListDataOrSearchBean>, var context: FragmentActivity?, var lostOrFound: String, var mylistView: MyListService.MyListView) : RecyclerView.Adapter<MyListTableAdapter.MyListViewHolder>() {


    class MyListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var myListItemStatus: TextView = view.findViewById(R.id.mylist_item_status)
        var myListItemTitle: TextView = view.findViewById(R.id.mylist_item_title)
        var myListItemType: TextView = view.findViewById(R.id.mylist_item_type)
        var myListItemTime: TextView = view.findViewById(R.id.mylist_item_time)
        var myListItemPlace: TextView = view.findViewById(R.id.mylist_item_place)
        var myListItemBackBlue: ImageView = view.findViewById(R.id.mylist_item_back_blue)
        var myListItemBackGrey: ImageView = view.findViewById(R.id.mylist_item_back_grey)
        var myListItemOutdata: ImageView = view.findViewById(R.id.mylist_item_back_outdata)
        var myListItemPencil: ImageView = view.findViewById(R.id.mylist_item_pencil)
        var myListItemPic: ImageView = view.findViewById(R.id.mylist_item_pic)
        var myListItemPencilTouch: TextView = view.findViewById(R.id.mylist_item_pencil_touch)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val (id: Int, _: Int, _: String, title: String, place: String, time: String, _: String, detail_type: Int, isback: Int, picture: List<String>?, _: String, _: Int, _: String, isExpired: Boolean) = myListBean[position]
        val intent = Intent()
        val bundle = Bundle()

        holder.apply {
            myListItemTitle.text = title
            myListItemType.text = Utils.getType(detail_type)
            myListItemTime.text = time
            myListItemPlace.text = if (Hawk.get<Int>("campus") == 1) "北洋园 - $place" else "卫津路 - $place"
            myListItemBackBlue.visibility = if (isback == 1) View.VISIBLE else View.GONE
            myListItemBackGrey.visibility = if (isback == 1) View.GONE else View.VISIBLE

            if (lostOrFound == "found") {
                holder.myListItemStatus.text = if (isback == 1) "已交还" else "未交还"
            } else {
                holder.myListItemStatus.text = if (isback == 1) "已找到" else "未找到"
            }

            myListItemStatus.setOnClickListener { mylistView.turnStatus(id) }
            myListItemBackBlue.setOnClickListener { mylistView.turnStatus(id) }
            myListItemBackGrey.setOnClickListener { mylistView.turnStatus(id) }
            myListItemStatus.setOnClickListener { mylistView.turnStatus(id) }
            myListItemBackBlue.setOnClickListener { mylistView.turnStatus(id) }
            myListItemBackGrey.setOnClickListener { mylistView.turnStatus(id) }

            if (isExpired) {//0是未过期
                myListItemBackBlue.visibility = View.GONE
                myListItemBackGrey.visibility = View.GONE
                myListItemOutdata.visibility = View.VISIBLE
                myListItemStatus.text = "已过期"
            }

            if (picture != null) {
                Glide.with(context)
                        .load(Utils.getPicUrl(picture[0]))
                        .placeholder(R.drawable.lf_waterfall_nopic)
                        .error(R.drawable.lf_waterfall_nopic)
                        .into(myListItemPic)
            } else {
                Glide.with(context)
                        .load(Utils.getPicUrl("julao.jpg"))
                        .placeholder(R.drawable.lf_waterfall_nopic)
                        .error(R.drawable.lf_waterfall_nopic)
                        .into(myListItemPic)
            }

            itemView.setOnClickListener {
                bundle.putInt("id", id)
                intent.putExtras(bundle)
                intent.setClass(context, DetailActivity::class.java)
                context?.startActivity(intent)
            }

            myListItemPencilTouch.setOnClickListener {
                bundle.apply {
                    if (lostOrFound == "lost") {
                        putString("lostOrFound", "editLost")
                    } else {
                        putString("lostOrFound", "editFound")
                    }
                    putInt("id", id)
                    putInt("type", detail_type)
                }

                intent.apply {
                    putExtras(bundle)
                    setClass(context, ReleaseActivity::class.java)
                    context?.startActivity(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.lf2_item_mylist, parent, false)
        return MyListViewHolder(view)
    }

    override fun getItemCount(): Int = myListBean.size
}




