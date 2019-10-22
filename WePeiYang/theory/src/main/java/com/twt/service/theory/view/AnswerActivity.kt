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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.orhanobut.hawk.Hawk
import com.twt.service.theory.R
import com.twt.service.theory.model.PaperBean
import com.twt.service.theory.model.TheoryApi
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
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
        var time = this@AnswerActivity.intent.extras.getInt("duration")
        theory_exam_time_fab.start(time, 0) {//这里处理时间走完的逻辑
        }
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
        theory_submit.setOnClickListener {// 提交答案
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
                    finish()
                }
            }
        }
    }

    private fun loadQuestions() {
        val recyclerView = findViewById<RecyclerView>(R.id.theory_exam_questions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        launch(UI) {
            try {
                val dat = TheoryApi.getPaper(intent.getIntExtra("id", 0)).await()
                //务必保证body非空
                AnswerManager.init(dat.body!!.size)//初始化答案管理器
                recyclerView.withItems {
                    for (i: PaperBean.BodyBean in dat.body!!) {
                        if (i.type == "sc") setSingleAnsQues(i)
                        else setMultiAnsQues(i)
                    }
                }
            } catch (e: Exception) {
                Toasty.error(this@AnswerActivity, "获取试卷失败, 请检查是否已连接校园网").show()
            }
        }
    }

}

