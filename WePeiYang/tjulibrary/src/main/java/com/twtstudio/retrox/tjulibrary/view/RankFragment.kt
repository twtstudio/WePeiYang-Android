package com.twtstudio.retrox.tjulibrary.view

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
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.DoubanApi
import com.twtstudio.retrox.tjulibrary.tjulibservice.LibraryApi
import com.twtstudio.retrox.tjulibrary.tjulibservice.RankList
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RankFragment : Fragment() {

    private var selectedRank = 666
    lateinit var recyclerView: RecyclerView
    private lateinit var loadMore: TextView
    private var startNum = 0
    private var endNum = 49
    private lateinit var rankList: List<RankList>
    private val urlList = ArrayList<String>(0)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.rank_page, container, false)
        loadMore = view.findViewById(R.id.load_more)
        val total = view.findViewById<TextView>(R.id.total)
        val month = view.findViewById<TextView>(R.id.month)
        val week = view.findViewById<TextView>(R.id.week)
        recyclerView = view.findViewById(R.id.rank_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        setRecyclerView()
        loadMore.visibility = View.GONE

        total.setOnClickListener {
            selectedRank = 666
            startNum = 0
            endNum = 49
            setRecyclerView()
        }

        month.setOnClickListener {
            selectedRank = 30
            startNum = 0
            endNum = 49
            setRecyclerView()
        }

        week.setOnClickListener {
            selectedRank = 7
            startNum = 0
            endNum = 49
            setRecyclerView()
        }



        return view
    }

    private fun setRecyclerView() {

        loadMore.visibility = if (endNum == 49) {
            View.GONE
        } else {
            View.VISIBLE
        }

        recyclerView.removeAllViews()
        launch(UI + QuietCoroutineExceptionHandler) {
            try {
                rankList = LibraryApi.getRank(selectedRank).await()
            }catch (e : Exception){
                Log.d("whatthefuck",e.toString())
            }

            recyclerView.withItems {
                rankList.forEachIndexed { index, rankList ->
                    setRankItem(rankList.bookID, "233", rankList.bookName, rankList.borrowNum, rankList.publisher, this@RankFragment, index + 1, false)
                }
            }
        }

    }
}