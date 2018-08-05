package com.twtstudio.service.tjwhm.exam.problem

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import com.twtstudio.service.tjwhm.exam.ext.FixedSpeedScroller
import com.twtstudio.service.tjwhm.exam.problem.score.ScoreActivity


class ProblemActivity : AppCompatActivity() {

    var mode: Int = 0
    var classID: Int = 0
    var time: Int = 0

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
    private lateinit var tvUpload: TextView
    private val pagerAdapter = ProblemPagerAdapter(supportFragmentManager)

    private var statusBarView: View? = null
    private var size = 0
    var userSelections: MutableMap<Int, UpdateResultViewModel> = mutableMapOf()

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
        tvUpload = findViewById(R.id.tv_problem_test_upload)

        mode = intent.getIntExtra(MODE_KEY, -999)
        classID = intent.getIntExtra(CLASS_ID_KEY, -999)

        val field = ViewPager::class.java.getDeclaredField("mScroller")
        field.isAccessible = true
        val scroller = FixedSpeedScroller(vpProblem.context)
        field.set(vpProblem, scroller)

        if (mode == CONTEST) {
            tvLeft.visibility = View.GONE
            tvRight.visibility = View.GONE
        } else if (mode == READ_AND_PRACTICE) {
            initTvLeftRight()
        }


        if (mode == READ_AND_PRACTICE) {
            tvUpload.visibility = View.GONE
            getIDs(classID.toString(), SINGLE_CHOICE.toString()) {
                when (it) {
                    is RefreshState.Failure -> {
                        Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    }
                    is RefreshState.Success -> {
                        for (i in 0 until it.message.ques.size) {
                            pagerAdapter.add(i, classID, SINGLE_CHOICE, ProblemFragment.READ_MODE, it.message.ques[i].id)
                        }
                        size += it.message.ques.size
                        vpProblem.adapter = pagerAdapter
                    }
                }
            }
        } else if (mode == CONTEST) {
            tvLeft.visibility = View.GONE
            tvRight.visibility = View.GONE
            tvUpload.visibility = View.VISIBLE
            tvUpload.setOnClickListener {
                uploadResult()
            }
            getTestProblems(classID.toString()) {
                when (it) {
                    is RefreshState.Failure -> {
                        Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    }
                    is RefreshState.Success -> {
                        for (i in 0 until it.message.data.size) {
                            pagerAdapter.add(i, it.message.data[i])
                        }
                        size += it.message.data.size
                        vpProblem.adapter = pagerAdapter
                        time = it.message.time
                    }
                }
            }
        }
    }

    fun storeResult(fragmentIndex: Int, updateResultViewModel: UpdateResultViewModel, scrollPage: Boolean) {
        userSelections[fragmentIndex] = updateResultViewModel
        if (scrollPage && vpProblem.currentItem < size - 1) {
            vpProblem.setCurrentItem(vpProblem.currentItem + 1, true)
        }
    }

    private fun uploadResult() {
        val gson = Gson()
        val list = mutableListOf<UpdateResultViewModel>()
        repeat(userSelections.size) {
            userSelections[it]?.let { it1 -> list.add(it1) }
        }
        val resultJsonString = gson.toJson(list)
        Log.d("zzzzzActivity  ", resultJsonString)
        getScore(classID.toString(), time.toString(), list) {
            when (it) {
                is RefreshState.Failure -> {
                    Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                }
                is RefreshState.Success -> {
                    Toasty.success(this@ProblemActivity, "交卷成功！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProblemActivity, ScoreActivity::class.java)
                    intent.putExtra(ScoreActivity.SCORE_VIEW_MODEL_KEY, it.message)
                    this@ProblemActivity.startActivity(intent)
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
