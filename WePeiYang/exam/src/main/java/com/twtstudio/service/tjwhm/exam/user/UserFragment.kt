package com.twtstudio.service.tjwhm.exam.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.user.history.HistoryActivity
import com.twtstudio.service.tjwhm.exam.user.star.StarActivity
import de.hdodenhof.circleimageview.CircleImageView

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_user, container, false)
        civAvatar = view.findViewById(R.id.civ_avatar)
        tvUserName = view.findViewById(R.id.tv_user_name)
        tvUserTitle = view.findViewById(R.id.tv_user_title)
        tvProblemsNum = view.findViewById(R.id.tv_already_problems_num)
        tvProblemsRadio = view.findViewById(R.id.tv_user_radio)
        tvLessonsNum = view.findViewById(R.id.tv_already_lesson_num)
        tvLessonsDetail = view.findViewById<TextView>(R.id.tv_user_lessons_detail).apply { visibility = View.GONE }
        llUserHistory = view.findViewById(R.id.ll_user_history)
        llUserWrong = view.findViewById(R.id.ll_user_wrong)
        llUserStar = view.findViewById(R.id.ll_user_star)

        llUserHistory.setOnClickListener {
            context?.startActivity(Intent(context, HistoryActivity::class.java))
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

    @SuppressLint("SetTextI18n")
    private fun bindExamUserData(userBean: UserBean) {
        userBean.let {
            Glide.with(context)
                    .load(it.avatar_url)
                    .into(civAvatar)

            tvUserName.text = it.twt_name
            tvUserTitle.text = it.title_name

            tvProblemsNum.text = it.done_count
            var radioText = ((it.error_count.toDouble() / it.done_count.toDouble()).toString() + "00").substring(2, 4)
            if (radioText[0] == '0') radioText = radioText.removeRange(0, 1)
            tvProblemsRadio.text = "$radioText%"
            tvLessonsNum.text = it.course_count.toString()
        }
    }
}