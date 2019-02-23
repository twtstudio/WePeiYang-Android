package com.twt.service.job.search


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.twt.service.job.R
import com.twt.service.job.service.*
import com.twt.service.job.story.StoryActivity
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty

class JobSearchResultFragment : Fragment(), JobSearchContract.JobSearchView {
    private lateinit var recyclerView: RecyclerView
    private val itemManager = ItemManager()
    private val searchPresenter = JobSearchPresenter(this)
    private lateinit var noResult: LinearLayout
    private var keyword: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.job_search_fragment, container, false)
        noResult = view.findViewById(R.id.job_search_no_res)
        noResult.visibility = View.GONE
        recyclerView = view.findViewById(R.id.job_search_rv_history)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ItemAdapter(itemManager)
        searchPresenter.searchKeyword(keyword)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as JobSearchActivity
        this.keyword = activity.keyword
    }

    override fun onSuccess(info: List<InfoOrMeeting>, meeting: List<InfoOrMeeting>) {
        recyclerView.withItems {
            if (info.isNotEmpty()) {
                repeat(info.size) { i ->
                    addInfo(info[i], i == 0) { startActivity(info[i].id, JOB_MESSAGE) }
                }
                repeat(meeting.size) { i ->
                    addMeeting(meeting[i], false) { startActivity(meeting[i].id, JOB_FAIR) }
                }
            } else {
                repeat(meeting.size) { i ->
                    addMeeting(meeting[i], i == 0) { startActivity(meeting[i].id, JOB_FAIR) }
                }
            }
        }
    }

    private fun startActivity(id: String, kind: String) {
        val intent = Intent(activity, StoryActivity::class.java)
        intent.putExtra(KEY_ID, id)
        intent.putExtra(KEY_KIND, kind)
        startActivity(intent)
    }

    override fun onNull() {
        noResult.visibility = View.VISIBLE
        val textView = noResult.findViewById<TextView>(R.id.job_search_tv_no_res)
        textView.text = "没有找到相关\"${keyword}\"的就业信息"
        recyclerView.withItems(mutableListOf())
    }

    override fun onError(msg: String) {
        Toasty.error(context!!, msg, Toast.LENGTH_LONG, true).show()
    }
}