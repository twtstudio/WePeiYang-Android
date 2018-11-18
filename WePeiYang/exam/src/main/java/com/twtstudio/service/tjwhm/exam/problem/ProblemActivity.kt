package com.twtstudio.service.tjwhm.exam.problem

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import com.twtstudio.service.tjwhm.exam.commons.FixedSpeedScroller
import com.twtstudio.service.tjwhm.exam.commons.joinQQGroupForHelp
import com.twtstudio.service.tjwhm.exam.problem.score.ScoreActivity


class ProblemActivity : AppCompatActivity(), ProblemActivityInterface {

    companion object {
        const val MODE_KEY = "problem_activity_mode"
        const val READ_AND_PRACTICE = 1
        const val CONTEST = 2

        const val LESSON_ID_KEY = "class_id_key"

        const val PROBLEM_TYPE_KEY = "problem_type_key"
        const val SINGLE_CHOICE = 0
        const val MULTI_CHOICE = 1
        const val TRUE_FALSE = 2

        const val CONTINUE_INDEX_KEY = "continue_index_key"

        var isLeft = true

    }

    // 当前的答题模式 (背题、测试), 题目类型 (单选、多选、判断)
    var mode: Int = 0
    private var problemType: Int = 0

    //  课程 ID, 本 activity 中的题目总数
    var lessonID: Int = 0
    var size: Int = 0

    private var continueIndex = 0
    var time: Int = 0
    var currentFragmentIndex = 0

    private var testBean: TestBean? = null

    private lateinit var clProblem: ConstraintLayout
    private lateinit var tvLeft: TextView
    private lateinit var tvRight: TextView
    private lateinit var vpProblem: ViewPager
    private lateinit var tvUpload: TextView
    private lateinit var ivHelp: ImageView
    private lateinit var problemIndexPopup: ProblemIndexPopup
    private val pagerAdapter = ProblemPagerAdapter(supportFragmentManager)

    private var startTime = 0L

    private var statusBarView: View? = null
    var userSelectionsForPractice: MutableList<List<Int>> = mutableListOf()
    var userSelectionsForTest: MutableMap<Int, UpdateResultViewModel> = mutableMapOf()
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
        ivHelp = findViewById<ImageView>(R.id.iv_problem_help).apply { setOnClickListener { joinQQGroupForHelp() } }

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
                continueIndex = intent.getIntExtra(CONTINUE_INDEX_KEY, 0)
                startReadAndPracticeNetwork()
            }

            CONTEST -> {
                tvLeft.visibility = View.GONE
                tvRight.visibility = View.GONE
                tvUpload.visibility = View.VISIBLE
                ivHelp.visibility = View.GONE
                startContestNetwork()
            }
        }

        vpProblem.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageSelected(position: Int) {
                currentFragmentIndex = position
                if (position > 1 && mode == READ_AND_PRACTICE)
                    write(lessonID.toString(), problemType.toString(), (currentFragmentIndex - 1).toString())
                mark(lessonID.toString(), problemType.toString(), (pagerAdapter.getItem(position) as ProblemFragment).problemID.toString(), currentFragmentIndex.toString())
            }
        })
        startTime = System.currentTimeMillis()
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
        getIDs(lessonID.toString(), problemType.toString()) { it ->
            when (it) {
                is RefreshState.Failure -> Toasty.error(this, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success -> {
                    it.message.data!!.apply {
                        this@ProblemActivity.size = size
                        repeat(size) {
                            userSelectionsForPractice.add(listOf())
                            problemIndexData.add(ProblemIndex.NONE)
                        }
                        for (i in 0 until size)
                            pagerAdapter.add(i, problemType, ProblemFragment.READ_MODE, this[i])

                        vpProblem.adapter = pagerAdapter
                        Toasty.success(this@ProblemActivity, "加载成功", Toast.LENGTH_SHORT).show()
                    }
                    vpProblem.currentItem = continueIndex
                }
            }
        }
    }

    private fun startContestNetwork() {
        getTestProblems(lessonID.toString()) { it ->
            when (it) {
                is RefreshState.Failure -> Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                is RefreshState.Success -> {
                    it.message.data!!.apply {
                        testBean = this
                        size = this.question.size
                        repeat(size) {
                            userSelectionsForTest[it] = UpdateResultViewModel(question[it].ques_id, "", question[it].ques_type)
                            problemIndexData.add(ProblemIndex.NONE)
                            pagerAdapter.add(it, question[it])
                        }
                        this@ProblemActivity.time = time
                        vpProblem.adapter = pagerAdapter
                        tvUpload.setOnClickListener { it ->
                            var answered = 0
                            repeat(size) {
                                if (userSelectionsForTest[it]?.answer != "")
                                    answered++
                            }
                            AlertDialog.Builder(it.context).apply {
                                setMessage("本次测试共${testBean?.question?.size}题\n\n你已完成${answered}题\n\n" +
                                        "是否交卷？")
                                setPositiveButton("交卷") { _, _ -> uploadResult() }
                                setNegativeButton("取消") { _, _ -> }
                            }.show()
                        }
                    }
                }
            }
        }
    }

    fun storeSelectionForPractice(index: Int, selections: List<Int>, isRight: Boolean) {
        userSelectionsForPractice[index] = selections
        if (isRight)
            problemIndexData[index] = ProblemIndex.TRUE
        else
            problemIndexData[index] = ProblemIndex.WRONG
    }

    fun storeResult(fragmentIndex: Int, updateResultViewModel: UpdateResultViewModel, problemIndex: ProblemIndex) {
        userSelectionsForTest[fragmentIndex] = updateResultViewModel
        problemIndexData[fragmentIndex] = problemIndex
        var answered = 0
        repeat(size) {
            if (userSelectionsForTest[it]?.answer != "")
                answered++
        }
        if (mode == CONTEST && answered == size - 1) {
            AlertDialog.Builder(this@ProblemActivity).apply {
                setMessage("本次测试共${testBean?.question?.size}题\n\n你全部完成\n\n" +
                        "是否交卷？")
                setPositiveButton("交卷") { _, _ -> uploadResult() }
                setNegativeButton("取消") { _, _ -> }
            }.show()
        } else if (fragmentIndex < size) {
            vpProblem.setCurrentItem(vpProblem.currentItem + 1, true)
        }
    }

    private fun uploadResult() {
        val list = mutableListOf<UpdateResultViewModel>()
        repeat(userSelectionsForTest.size) {
            userSelectionsForTest[it]?.let { it1 -> list.add(it1) }
        }
        getScore(lessonID.toString(), time.toString(), list) {
            when (it) {
                is RefreshState.Failure -> {
                    Toasty.error(this@ProblemActivity, "网络错误", Toast.LENGTH_SHORT).show()
                    Log.e("ProblemActivity", it.throwable.toString())
                }
                is RefreshState.Success -> {
                    Toasty.success(this@ProblemActivity, "交卷成功！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProblemActivity, ScoreActivity::class.java)
                    intent.putExtra(ScoreActivity.TEST_TIME_KEY, System.currentTimeMillis() - startTime)
                    intent.putExtra(ScoreActivity.SCORE_BEAN_KEY, it.message.data)
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
        problemIndexPopup = ProblemIndexPopup(this@ProblemActivity, this@ProblemActivity, Pair(llX, llY), problemIndexData)
        problemIndexPopup.show()
    }

    override fun onProblemIndexItemClick(index: Int) {
        if (index in 0 until size) vpProblem.currentItem = index
        problemIndexPopup.dismiss()
    }

    override fun onBackPressed() {
        if (mode == READ_AND_PRACTICE || testBean == null)
            super.onBackPressed()
        else {
            var answered = 0
            repeat(size) {
                if (userSelectionsForTest[it]?.answer != "")
                    answered++
            }
            AlertDialog.Builder(this@ProblemActivity).apply {
                this.setTitle("\n本次测试共${testBean?.question?.size}题,你已完成${answered}题")
                setMessage("直接退出将不保存此次做题记录\n是否交卷？")
                setPositiveButton("交卷") { _, _ -> uploadResult() }
                setNegativeButton("取消") { _, _ -> Unit }
                setNeutralButton("直接退出") { _, _ -> finish() }
            }.show()
        }
    }

    private fun initStatusBar() {
        if (statusBarView == null) {
            val identifier = resources.getIdentifier("statusBarBackground", "id", "android")
            statusBarView = window.findViewById(identifier)
        }
        statusBarView?.setBackgroundResource(R.drawable.exam_shape_statusbar_gradient)
    }
}

interface ProblemActivityInterface {
    fun onProblemIndexItemClick(index: Int)
}
