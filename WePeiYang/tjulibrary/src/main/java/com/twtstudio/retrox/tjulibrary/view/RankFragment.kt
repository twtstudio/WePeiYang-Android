package com.twtstudio.retrox.tjulibrary.view

import android.graphics.Color
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.LibraryApi
import com.twtstudio.retrox.tjulibrary.tjulibservice.RankList
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.backgroundColor

class RankFragment : Fragment() {

    private var selectedRank = 7
    lateinit var recyclerView: RecyclerView
    private var startNum = 0
    private var endNum = 50
    private lateinit var rankList: List<RankList>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.rank_page, container, false)
        val total = view.findViewById<TextView>(R.id.total)
        val month = view.findViewById<TextView>(R.id.month)
        val week = view.findViewById<TextView>(R.id.week)
        recyclerView = view.findViewById(R.id.rank_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        setRecyclerView()
        week.backgroundColor = Color.parseColor("#f7f7f7")

        total.setOnClickListener {
            selectedRank = 666
            startNum = 0
            endNum = 50
            setRecyclerView()
            total.backgroundColor = Color.parseColor("#f7f7f7")
            month.backgroundColor = Color.parseColor("#eeeeee")
            week.backgroundColor = Color.parseColor("#eeeeee")
        }

        month.setOnClickListener {
            selectedRank = 30
            startNum = 0
            endNum = 50
            setRecyclerView()
            total.backgroundColor = Color.parseColor("#eeeeee")
            month.backgroundColor = Color.parseColor("#f7f7f7")
            week.backgroundColor = Color.parseColor("#eeeeee")
        }

        week.setOnClickListener {
            selectedRank = 7
            startNum = 0
            endNum = 50
            setRecyclerView()
            total.backgroundColor = Color.parseColor("#eeeeee")
            month.backgroundColor = Color.parseColor("#eeeeee")
            week.backgroundColor = Color.parseColor("#f7f7f7")
        }

        return view
    }

    private fun setRecyclerView() {
        recyclerView.removeAllViews()
        launch(UI + QuietCoroutineExceptionHandler) {
            try {
                rankList = LibraryApi.getRank(selectedRank).await()
            }catch (e : Exception){
                Log.d("whatthefuck",e.toString())
            }

            recyclerView.withItems {
                rankList.forEachIndexed { index, rankList ->
                    setRankItem(rankList.bookID,
                            "error",
                            rankList.bookName,
                            rankList.borrowNum,
                            rankList.publisher,
                            this@RankFragment,
                            index + 1,
                            false)
                }
            }
        }

    }
}