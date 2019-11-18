package com.twt.service.theory.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.orhanobut.hawk.Hawk
import com.twt.service.theory.R
import com.twt.service.theory.model.PaperBean
import com.twt.service.theory.model.ScoreBean
import com.twt.service.theory.model.TheoryApi
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.hawk
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.theory_activity_answer.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_dialog_exam.view.*
import kotlinx.android.synthetic.main.theory_popupwindow_layout.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip

@RequiresApi(Build.VERSION_CODES.M)
class AnswerActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        theory_exam_time_fab.end()
    }

    override fun onBackPressed() {
        if (AnswerManager.isPopUPWindowInstalled()) {
            AnswerManager.getPopUpWindow()?.dismiss()
            AnswerManager.uninstall()
        } else {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_dialog_exam, null, false)
            view.theory_enter_word.text = "你将退出考试"
            popupWindow.apply {
                isFocusable = true
                contentView = view
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                width = dip(320)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.CENTER,
                        0,
                        0
                )
                contentView.theory_enter_cancel.setOnClickListener {
                    popupWindow.dismiss()
                }
                contentView.theory_enter_comfirm.setOnClickListener {
                    popupWindow.dismiss()
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_activity_answer)
        initView()
        loadQuestions()
        theory_user_actionbar.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        theory_constraintLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        theory_exam_time_fab.actionH = theory_user_actionbar.measuredHeight
        theory_exam_time_fab.ansH = theory_constraintLayout.measuredHeight

        Hawk.put("theory_answer_lock", true)//设置答题锁，表示当前有了一个答题过程
    }

    private fun initView() {
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableLightStatusBarMode(true)
        theory_submit.visibility = View.VISIBLE
        theory_person_profile.visibility = View.GONE
        theory_search.visibility = View.GONE
        theory_constraintLayout.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_popupwindow_layout, null, false)
            popupWindow.apply {
                contentView = view
                width = WindowManager.LayoutParams.MATCH_PARENT
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                height = dip(280)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.BOTTOM,
                        0,
                        0
                )
                contentView.theory_constraintLayout2.setOnClickListener {
                    popupWindow.dismiss()
                    AnswerManager.uninstall()
                }
                contentView.theory_recyclerView.layoutManager = GridLayoutManager(contentView.context, 6, GridLayoutManager.VERTICAL, false)
            }
            AnswerManager.installPopUpWindow(popupWindow, theory_exam_questions)
        }
        theory_back.setOnClickListener {
            onBackPressed()
        }
        theory_submit.setOnClickListener {
            // 提交答案
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_dialog_exam, null, false)
            view.theory_enter_word.text = if (AnswerManager.getNumberHasDone() != AnswerManager.getTotalNumber()) "你还有${AnswerManager.getTotalNumber() - AnswerManager.getNumberHasDone()}题未完成，确认提交?"
            else "你将提交答案"
            popupWindow.apply {
                isFocusable = true
                contentView = view
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                width = dip(320)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.CENTER,
                        0,
                        0
                )
                contentView.theory_enter_cancel.setOnClickListener {
                    popupWindow.dismiss()
                }
                contentView.theory_enter_comfirm.setOnClickListener {
                    popupWindow.dismiss()
                    val scoreBean = ScoreBean()
                    val qList = AnswerManager.getQustionList()
                    var at = 0
                    scoreBean.paper_id = AnswerManager.getTestId()
                    while (at < qList.size) {
                        val answerBean = ScoreBean.AnswerBean()
                        var ans = AnswerManager.getAnswer(at + 1)
                        if (ans != 0) {
                            if (qList[at].type == "sc") {
                                answerBean.id = qList[at].id.toString()
                                answerBean.ans = ('A' + ans - 1).toString()
                                scoreBean.scq.add(answerBean)
                            } else {
                                answerBean.id = qList[at].id.toString()
                                answerBean.ans = ""
                                var to = 0
                                while (to < 6) {
                                    if (ans % 10 == 1) answerBean.ans = answerBean.ans + ('A' + to).toString()
                                    ans /= 10
                                    ++to
                                }
                            }
                        }
                        ++at
                    }
                    Log.d("BUG", scoreBean.toString())
                    launch(UI) {
                        try {
                            val dat = TheoryApi.getScore(scoreBean.toString()).await()
                            Log.d("BUG", dat.string())
                            Toasty.success(this@AnswerActivity, "提交成功,你的成绩是${dat.string()}").show()

                            finish()
                        } catch (e: Exception) {
                            Toasty.error(this@AnswerActivity, "提交失败").show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun loadQuestions() {
        val recyclerView = findViewById<RecyclerView>(R.id.theory_exam_questions)
        val lock = this.intent.extras.getBoolean("lock")//获取加载锁
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (!lock) {//需要网络加载
            launch(UI) {
                try {
                    val dat = TheoryApi.getPaper(intent.getIntExtra("id", 0)).await()
                    val list = mutableListOf<PaperBean.BodyBean>()//题目的id编号队列
                    //务必保证body非空
                    recyclerView.withItems {
                        for (i: PaperBean.BodyBean in dat.body!!) {
                            if (i.type == "sc") setSingleAnsQues(i)
                            else setMultiAnsQues(i)
                            list.add(i)//把题号放进去
                        }
                    }
                    val time = this@AnswerActivity.intent.extras.getInt("duration")//单位为秒
                    AnswerManager.init(dat.body!!.size, list, dat.head!!.id, System.currentTimeMillis() / 1000, time)
                    //初始化答案管理器
                    theory_exam_time_fab.start(time / 60, time % 60) {
                        //这里处理时间走完的逻辑
                    }
                } catch (e: Exception) {
                    Toasty.error(this@AnswerActivity, "获取试卷失败, 请检查是否已连接校园网").show()
                }
            }
        } else {
            val qList = AnswerManager.getQustionList()
            val time = this@AnswerActivity.intent.extras.getInt("duration")
            recyclerView.withItems {
                for (i in qList) {
                    if (i.type == "sc") setSingleAnsQues(i)
                    else setMultiAnsQues(i)
                }
            }
            AnswerManager.setBeginTIme(System.currentTimeMillis() / 1000)
            AnswerManager.setDuration(time)
            theory_exam_time_fab.start(time / 60, time % 60) {
                //这里处理时间走完的逻辑
            }
        }
    }

}

