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


class SearchAdapter(var hr: MutableList<String>?, val activity: SearchInitActivity) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDiv: View = view.findViewById(R.id.tv_search_div)
        val textView: TextView = view.findViewById(R.id.tv_search_hr)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.textView.text = hr!![position]
        holder.textView.setOnClickListener {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString("query", hr!![position])
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