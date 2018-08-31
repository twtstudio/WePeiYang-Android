package com.twtstudio.service.tjwhm.exam.user

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.user.history.HistoryActivity
import com.twtstudio.service.tjwhm.exam.user.star.StarActivity
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty

class UserFragment : Fragment() {
    companion object {
        fun newInstance(): UserFragment = UserFragment()
    }

    private lateinit var civAvatar: CircleImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserTitle: TextView
    private lateinit var tvProblemsNum: TextView
    private lateinit var tvProblemsRadio: TextView
    private lateinit var tvLessonsNum: TextView
    private lateinit var tvLessonsDetail: TextView
    private lateinit var llUserHistory: LinearLayout
    private lateinit var llUserWrong: LinearLayout
    private lateinit var llUserStar: LinearLayout

    private var hasHistory = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_user, container, false)
        civAvatar = view.findViewById(R.id.civ_avatar)
        tvUserName = view.findViewById(R.id.tv_user_name)
        tvUserTitle = view.findViewById(R.id.tv_user_title)
        tvProblemsNum = view.findViewById(R.id.tv_already_problems_num)
        tvProblemsRadio = view.findViewById(R.id.tv_user_radio)
        tvLessonsNum = view.findViewById(R.id.tv_already_lesson_num)
        tvLessonsDetail = view.findViewById(R.id.tv_user_lessons_detail)
        llUserHistory = view.findViewById(R.id.ll_user_history)
        llUserWrong = view.findViewById(R.id.ll_user_wrong)
        llUserStar = view.findViewById(R.id.ll_user_star)


        llUserHistory.setOnClickListener {
            if (hasHistory) context?.startActivity(Intent(context, HistoryActivity::class.java))
            else this@UserFragment.context?.let { it1 -> Toasty.info(it1, "你还没有刷题历史哦", Toast.LENGTH_SHORT).show() }
        }

        llUserStar.setOnClickListener {
            val intent = Intent(context, StarActivity::class.java)
            intent.putExtra(StarActivity.TYPE_KEY, StarActivity.STAR)
            context?.startActivity(intent)
        }

        llUserWrong.setOnClickListener {
            val intent = Intent(context, StarActivity::class.java)
            intent.putExtra(StarActivity.TYPE_KEY, StarActivity.WRONG)
            context?.startActivity(intent)
        }
        examUserLiveData.bindNonNull(this, ::bindExamUserData)
        return view
    }

    private fun bindExamUserData(examUserViewModel: ExamUserViewModel) {
        examUserViewModel.data.let {
            Glide.with(context)
                    .load(it.avatar_url)
                    .into(civAvatar)
            tvUserName.text = it.twt_name
            tvUserTitle.text = it.title.title_name

            val problemNum = it.ques_message.done_number.toInt() + it.ques_message.remember_number.toInt()
            tvProblemsNum.text = problemNum.toString()
            tvProblemsRadio.text = ((it.ques_message.error_number.toDouble() / problemNum.toDouble()).toString() + "000").substring(2, 4) + "%"
            tvLessonsNum.text = it.ques_message.remember_course_number.toString()

            hasHistory = it.history.status == 1
        }
    }
}