package com.example.lostfond2.release

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class ReleasePicadapter(val list : ArrayList<String>,
                        val releaseActivity: ReleaseActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int = list.size

}