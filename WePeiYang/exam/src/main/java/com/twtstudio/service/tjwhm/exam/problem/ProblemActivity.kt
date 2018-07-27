package com.twtstudio.service.tjwhm.exam.problem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty

class ProblemActivity : AppCompatActivity() {

    var mode: Int = 0
    var classID: Int = 0

    companion object {
        const val MODE_KEY = "problem_activity_mode"
        const val READ_AND_PRACTICE = 1
        const val CONTEST = 2

        const val CLASS_ID_KEY = "class_id_key"

        const val SINGLE_CHOICE = 0
        const val MUTLI_CHOICE = 1
        const val TRUE_FASLE = 2

        var isLeft = true
    }

    private lateinit var tvLeft: TextView
    private lateinit var tvRight: TextView
    private lateinit var vpProblem: ViewPager
    private val pagerAdapter = ProblemPagerAdapter(supportFragmentManager)

    private var statusBarView: View? = null
    private var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_problem)
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }


        findViewById<ImageView>(R.id.iv_problem_back).setOnClickListener { onBackPressed() }
        tvLeft = findViewById(R.id.tv_problem_left)
        tvRight = findViewById(R.id.tv_problem_right)
        vpProblem = findViewById(R.id.vp_problem)

        mode = intent.getIntExtra(MODE_KEY, -999)
        classID = intent.getIntExtra(CLASS_ID_KEY, -999)

        if (mode == CONTEST) {
            tvLeft.visibility = View.GONE
            tvRight.visibility = View.GONE
        } else if (mode == READ_AND_PRACTICE) {
            initTvLeftRight()
        }


        if (mode == READ_AND_PRACTICE) {
            getIDs(classID.toString(), SINGLE_CHOICE.toString()) {
                when (it) {
                    is RefreshState.Failure -> {
                        Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    }
                    is RefreshState.Success -> {
                        for (i in 0 until it.message.ques.size) {
                            pagerAdapter.add(classID, SINGLE_CHOICE, ProblemFragment.READ_MODE, it.message.ques[i].id)
                        }
                        size += it.message.ques.size
                        vpProblem.adapter = pagerAdapter
                    }
                }
            }
        } else if (mode == CONTEST) {
            tvLeft.visibility = View.GONE
            tvRight.visibility = View.GONE
            getTestProblems(classID.toString()) {
                when (it) {
                    is RefreshState.Failure -> {
                        Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    }
                    is RefreshState.Success -> {
                        for (i in 0 until it.message.data.size) {
                            pagerAdapter.add(it.message.data[i])
                        }
                        size += it.message.data.size
                        vpProblem.adapter = pagerAdapter
                    }
                }
            }
        }
    }

    private fun initTvLeftRight() {
        tvLeft.apply {
            setBackgroundResource(R.drawable.exam_selected_left)
            setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.examBlueText))
            setOnClickListener {
                if (!isLeft) {
                    setBackgroundResource(R.drawable.exam_selected_left)
                    setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.examBlueText))
                    tvRight.setBackgroundResource(R.drawable.exam_not_selected_right)
                    tvRight.setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.white_color))
                    isLeft = true
                    pagerAdapter.changeMode(vpProblem.currentItem)
                    if (vpProblem.currentItem != 0) pagerAdapter.changeMode(vpProblem.currentItem - 1)
                    if (vpProblem.currentItem != size - 1) pagerAdapter.changeMode(vpProblem.currentItem + 1)
                    pagerAdapter.notifyDataSetChanged()
                }
            }
        }

        tvRight.setOnClickListener {
            if (isLeft) {
                tvRight.setBackgroundResource(R.drawable.exam_selected_right)
                tvRight.setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.examBlueText))
                tvLeft.setBackgroundResource(R.drawable.exam_not_selected_left)
                tvLeft.setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.white_color))
                isLeft = false
                pagerAdapter.changeMode(vpProblem.currentItem)
                if (vpProblem.currentItem != 0) pagerAdapter.changeMode(vpProblem.currentItem - 1)
                if (vpProblem.currentItem != size - 1) pagerAdapter.changeMode(vpProblem.currentItem + 1)
                pagerAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_statusbar_gradient)
    }
}
