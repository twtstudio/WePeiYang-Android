package com.twtstudio.retrox.tjulibrary.view

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    private lateinit var itemManager: ItemManager
    private lateinit var loadMore : TextView
    private var startNum = 0
    private var endNum = 9
    private lateinit var rankList : List<RankList>
    private val urlList = ArrayList<String>(0)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.rank_page, container, false)
        loadMore = view.findViewById(R.id.load_more)
        val total = view.findViewById<TextView>(R.id.total)
        val month = view.findViewById<TextView>(R.id.month)
        val week = view.findViewById<TextView>(R.id.week)
        recyclerView = view.findViewById(R.id.rank_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        itemManager = ItemManager()
//        recyclerView.adapter = ItemAdapter(itemManager)
        setRecyclerView()

        total.setOnClickListener {
            selectedRank = 666
            startNum = 0
            endNum = 9
            setRecyclerView()
        }

        month.setOnClickListener {
            selectedRank = 30
            startNum = 0
            endNum = 9
            setRecyclerView()
        }

        week.setOnClickListener {
            selectedRank = 7
            startNum = 0
            endNum = 9
            setRecyclerView()
        }

        loadMore.visibility = if (endNum == 49) { View.GONE  } else { View.VISIBLE }

        return view
    }

    private fun setRecyclerView() {

        recyclerView.removeAllViews()
        launch(UI + QuietCoroutineExceptionHandler) {
            rankList = LibraryApi.getRank(selectedRank).await()

            for (i in startNum..endNum) {
                val imgbean = LibraryApi.getImg(rankList[i].bookID).await()
                urlList.add(imgbean.img_url)
            }
            recyclerView.withItems {
                for (i in startNum..endNum) {
                    setRankItem(rankList[i].bookID, urlList[i], rankList[i].bookName, rankList[i].borrowNum, rankList[i].publisher, this@RankFragment, i + 1, false)
                }
            }
        }

        loadMore.setOnClickListener {
            startNum += 10
            endNum += 10

            launch(UI + QuietCoroutineExceptionHandler) {
                rankList = LibraryApi.getRank(selectedRank).await()

                for (i in startNum..endNum) {
                    val imgbean = LibraryApi.getImg(rankList[i].bookID).await()

                    itemManager.add(RankItem(rankList[i].bookID, imgbean.img_url, rankList[i].bookName, rankList[i].borrowNum, rankList[i].publisher, this@RankFragment, i + 1, false))
                }
            }
        }
    }
}