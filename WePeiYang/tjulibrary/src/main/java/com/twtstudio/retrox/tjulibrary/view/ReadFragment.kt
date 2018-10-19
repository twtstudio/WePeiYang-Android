package com.twtstudio.retrox.tjulibrary.view

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twtstudio.retrox.tjulibrary.R

class ReadFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.lib_fragment_read,container,false)
        return view
    }
}