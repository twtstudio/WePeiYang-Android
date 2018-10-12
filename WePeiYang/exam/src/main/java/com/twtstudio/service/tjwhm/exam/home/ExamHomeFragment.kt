package com.twtstudio.service.tjwhm.exam.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.toProblemType
import com.twtstudio.service.tjwhm.exam.list.ListActivity
import com.twtstudio.service.tjwhm.exam.user.UserBean
import com.twtstudio.service.tjwhm.exam.user.examUserLiveData
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

class ExamHomeFragment : Fragment(), View.OnClickListener {

    private lateinit var ivParty: ImageView
    private lateinit var ivPolicy: ImageView
    private lateinit var ivOnline: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var tvParty: TextView
    private lateinit var tvPolicy: TextView
    private lateinit var tvOnline: TextView
    private lateinit var tvMore: TextView
    private lateinit var tvNews: TextView
    private lateinit var tvNewsTime: TextView
    private lateinit var rvQuick: RecyclerView
    private lateinit var tvCurrentTitle: TextView
    private lateinit var tvCurrentType: TextView
    private lateinit var tvCurrentIndex: TextView
    private lateinit var tvCurrentNum: TextView

    companion object {
        fun newInstance(): ExamHomeFragment = ExamHomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_home, container, false)
        ivParty = view.findViewById(R.id.iv_party)
        ivParty.setOnClickListener(this)
        ivPolicy = view.findViewById(R.id.iv_policy)
        ivPolicy.setOnClickListener(this)
        ivOnline = view.findViewById(R.id.iv_online)
        ivOnline.setOnClickListener(this)
        ivMore = view.findViewById(R.id.iv_more)
        ivMore.setOnClickListener(this)
        tvParty = view.findViewById(R.id.tv_party)
        tvParty.setOnClickListener(this)
        tvPolicy = view.findViewById(R.id.tv_policy)
        tvPolicy.setOnClickListener(this)
        tvOnline = view.findViewById(R.id.tv_online)
        tvOnline.setOnClickListener(this)
        tvMore = view.findViewById(R.id.tv_more)
        tvMore.setOnClickListener(this)
        tvNews = view.findViewById(R.id.tv_news)
        tvNewsTime = view.findViewById(R.id.tv_news_time)
        rvQuick = view.findViewById(R.id.rv_quick)
        tvCurrentTitle = view.findViewById(R.id.tv_current_title)
        tvCurrentType = view.findViewById(R.id.tv_current_type)
        tvCurrentIndex = view.findViewById(R.id.tv_current_index)
        tvCurrentNum = view.findViewById(R.id.tv_current_num)

        rvQuick.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)

        examUserLiveData.bindNonNull(this, ::bindHomeData)
        return view
    }

    override fun onClick(v: View?) {
        val intent = Intent(context, ListActivity::class.java)
        when (v) {
            ivParty, tvParty -> {
                intent.putExtra(ListActivity.LESSON_TYPE, ListActivity.PARTY)
                context?.startActivity(intent)
            }
            ivPolicy, tvPolicy -> {
                intent.putExtra(ListActivity.LESSON_TYPE, ListActivity.POLICY)
                context?.startActivity(intent)
            }
            ivOnline, tvOnline -> {
                intent.putExtra(ListActivity.LESSON_TYPE, ListActivity.ONLINE)
                context?.startActivity(intent)
            }
            ivMore, tvMore -> activity?.let { Toasty.info(it, "暂无其他类课程!", Toast.LENGTH_SHORT).show() }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindHomeData(userBean: UserBean) {
        rvQuick.withItems {
            repeat(userBean.qSelect.size) { index ->
                context?.let {
                    this@ExamHomeFragment.activity?.let { it1 -> quickSelectItem(it1, userBean.qSelect[index].id, userBean.qSelect[index].course_name) }
                }
            }
        }
        tvNews.text = "${userBean.latest_course_name}已更新"
        tvNewsTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(userBean.latest_course_timestamp.toLong() * 1000L))
        tvCurrentTitle.text = userBean.current_course_name
        tvCurrentType.text = userBean.current_ques_type.toInt().toProblemType()
        tvCurrentIndex.text = "当前题目：${userBean.current_course_index}"
        tvCurrentNum.text = "进度：${userBean.current_course_done_count}/${userBean.current_course_ques_count}"
    }
}
