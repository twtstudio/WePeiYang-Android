package com.twtstudio.service.tjwhm.exam.problem

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import com.twtstudio.service.tjwhm.exam.commons.FixedSpeedScroller
import com.twtstudio.service.tjwhm.exam.problem.score.ScoreActivity


class ProblemActivity : AppCompatActivity() {

    companion object {
        const val MODE_KEY = "problem_activity_mode"
        const val READ_AND_PRACTICE = 1
        const val CONTEST = 2

        const val LESSON_ID_KEY = "class_id_key"

        const val PROBLEM_TYPE_KEY = "problem_type_key"
        const val SINGLE_CHOICE = 0
        const val MULTI_CHOICE = 1
        const val TRUE_FALSE = 2

        var isLeft = true
    }

    var mode: Int = 0
    var lessonID: Int = 0
    var problemType: Int = 0
    var time: Int = 0
    var currentFragmentIndex = 0

    private lateinit var problemForTest: TestViewModel

    private lateinit var clProblem: ConstraintLayout
    private lateinit var tvLeft: TextView
    private lateinit var tvRight: TextView
    private lateinit var vpProblem: ViewPager
    private lateinit var tvUpload: TextView
    private lateinit var problemIndexPopup: ProblemIndexPopup
    private val pagerAdapter = ProblemPagerAdapter(supportFragmentManager)

    private var statusBarView: View? = null
    var size = 0
    var userSelections: MutableMap<Int, UpdateResultViewModel> = mutableMapOf()
    private var problemIndexData: MutableList<ProblemIndex> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.exam_activity_problem)
        isLeft = true
        Looper.myQueue().addIdleHandler {
            initStatusBar()
            window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> initStatusBar() }
            false
        }
        Toasty.info(this@ProblemActivity, "正在加载", Toast.LENGTH_SHORT).show()

        findViewById<ImageView>(R.id.iv_problem_back).setOnClickListener { onBackPressed() }

        clProblem = findViewById(R.id.cl_problem)
        tvLeft = findViewById(R.id.tv_problem_left)
        tvRight = findViewById(R.id.tv_problem_right)
        vpProblem = findViewById(R.id.vp_problem)
        tvUpload = findViewById(R.id.tv_problem_test_upload)

        mode = intent.getIntExtra(MODE_KEY, -999)
        lessonID = intent.getIntExtra(LESSON_ID_KEY, -999)

        val field = ViewPager::class.java.getDeclaredField("mScroller")
        field.isAccessible = true
        val scroller = FixedSpeedScroller(vpProblem.context)
        field.set(vpProblem, scroller)

        when (mode) {
            READ_AND_PRACTICE -> {
                initTvLeftRight()
                tvUpload.visibility = View.GONE
                problemType = intent.getIntExtra(PROBLEM_TYPE_KEY, 0)
                startReadAndPracticeNetwork()
            }

            CONTEST -> {
                tvLeft.visibility = View.GONE
                tvRight.visibility = View.GONE

                tvUpload.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        uploadResult()
                    }
                }
                startContestNetwork()
            }
        }

        vpProblem.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageSelected(position: Int) {
                currentFragmentIndex = position
            }
        })
    }

    private fun initTvLeftRight() {
        tvLeft.apply {
            setBackgroundResource(R.drawable.exam_shape_selected_left)
            setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.examTextBlue))
            setOnClickListener {
                if (!isLeft) {
                    setBackgroundResource(R.drawable.exam_shape_selected_left)
                    setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.examTextBlue))
                    tvRight.setBackgroundResource(R.drawable.exam_shape_not_selected_right)
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
                tvRight.setBackgroundResource(R.drawable.exam_shape_selected_right)
                tvRight.setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.examTextBlue))
                tvLeft.setBackgroundResource(R.drawable.exam_shape_not_selected_left)
                tvLeft.setTextColor(ContextCompat.getColor(this@ProblemActivity, R.color.white_color))
                isLeft = false
                pagerAdapter.changeMode(vpProblem.currentItem)
                if (vpProblem.currentItem != 0) pagerAdapter.changeMode(vpProblem.currentItem - 1)
                if (vpProblem.currentItem != size - 1) pagerAdapter.changeMode(vpProblem.currentItem + 1)
                pagerAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun startReadAndPracticeNetwork() {
        getIDs(lessonID.toString(), problemType.toString()) {
            when (it) {
                is RefreshState.Failure -> Toasty.error(this, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success -> {
                    it.message.data!!.apply {
                        for (i in 0 until this.size)
                            pagerAdapter.add(i, problemType, ProblemFragment.READ_MODE, this[i])

                        vpProblem.adapter = pagerAdapter
                        Toasty.success(this@ProblemActivity, "加载成功", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startContestNetwork() {
        getTestProblems(lessonID.toString()) {
            when (it) {
                is RefreshState.Failure -> Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success -> {
                    problemForTest = it.message
                    for (i in 0 until it.message.data.size) {
                        pagerAdapter.add(i, it.message.data[i])
                    }
                    size += it.message.data.size
                    vpProblem.adapter = pagerAdapter
                    time = it.message.time
                    repeat(size) {
                        problemIndexData.add(ProblemIndex.NONE)
                    }
                }
            }
        }
    }


    fun storeResult(fragmentIndex: Int, updateResultViewModel: UpdateResultViewModel, problemIndex: ProblemIndex, scrollPage: Boolean) {
        userSelections[fragmentIndex] = updateResultViewModel
        problemIndexData[fragmentIndex] = problemIndex
        if (mode == CONTEST && userSelections.size == size) {
            // todo
        } else if (scrollPage && vpProblem.currentItem < size - 1) {
            vpProblem.setCurrentItem(vpProblem.currentItem + 1, true)
        }
    }

    private fun uploadResult() {
        if (size != userSelections.size) {
            Toasty.info(this@ProblemActivity, "请完成所有题目", Toast.LENGTH_SHORT).show()
            showProblemIndexPopupWindow(tvUpload.x, tvUpload.y, currentFragmentIndex)
            return
        }
        val list = mutableListOf<UpdateResultViewModel>()
        repeat(userSelections.size) {
            userSelections[it]?.let { it1 -> list.add(it1) }
        }
        getScore(lessonID.toString(), time.toString(), list) {
            when (it) {
                is RefreshState.Failure -> {
                    Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                }
                is RefreshState.Success -> {
                    Toasty.success(this@ProblemActivity, "交卷成功！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProblemActivity, ScoreActivity::class.java)
                    intent.putExtra(ScoreActivity.SCORE_VIEW_MODEL_KEY, it.message)
                    intent.putExtra(ScoreActivity.PROBLEM_FOR_TEST_KEY, problemForTest)
                    this@ProblemActivity.startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun showProblemIndexPopupWindow(llX: Float, llY: Float, fragmentIndex: Int) {
        repeat(problemIndexData.size) {
            when (problemIndexData[it]) {
                is ProblemIndex.NOW.NONE -> problemIndexData[it] = ProblemIndex.NONE
                is ProblemIndex.NOW.TRUE -> problemIndexData[it] = ProblemIndex.TRUE
                is ProblemIndex.NOW.WRONG -> problemIndexData[it] = ProblemIndex.WRONG
            }
        }

        when (problemIndexData[fragmentIndex]) {
            is ProblemIndex.NONE -> problemIndexData[fragmentIndex] = ProblemIndex.NOW.NONE
            is ProblemIndex.TRUE -> problemIndexData[fragmentIndex] = ProblemIndex.NOW.TRUE
            is ProblemIndex.WRONG -> problemIndexData[fragmentIndex] = ProblemIndex.NOW.WRONG
        }
        problemIndexPopup = ProblemIndexPopup(this@ProblemActivity, Pair(llX, llY), problemIndexData)
        problemIndexPopup.show()
    }

    fun onProblemIndexItemClick(index: Int) {
        if (index in 0 until size) vpProblem.currentItem = index
        problemIndexPopup.dismiss()
    }

    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_shape_statusbar_gradient)
    }
}
