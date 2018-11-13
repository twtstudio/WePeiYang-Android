package com.yookiely.lostfond2.search

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk


class SearchAdapter(var hr: MutableList<String>?, val activity: SearchInitActivity) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDiv: View = view.findViewById(R.id.tv_search_div)
        val textView: TextView = view.findViewById(R.id.tv_search_hr)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val query = hr!![position]
        holder.textView.text = query
        holder.textView.setOnClickListener {
            var temp = mutableListOf<String>()
            if (Hawk.get<MutableList<String>>("lf_search") != null) {
                temp = Hawk.get<MutableList<String>>("lf_search")
                temp.remove(query)
            }
            temp.add(query)
            if (temp.size > 5) {
                temp.removeAt(0)
            }
            Hawk.put("lf_search", temp)
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString("query", query)
            intent.putExtras(bundle)
            intent.setClass(activity, SearchActivity::class.java)
            startActivity(activity, intent, bundle)
        }
        if (hr!!.size == position) {
            holder.textViewDiv.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = hr!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf2_search_hr_rv_item, parent, false)
        return ViewHolder(view)
    }
}