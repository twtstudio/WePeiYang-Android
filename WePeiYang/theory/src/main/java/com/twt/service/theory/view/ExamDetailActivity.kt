package anim

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.twt.service.theory.R
import com.twt.service.theory.view.AnswerActivity
import com.twt.service.theory.view.AnswerManager
import com.twt.service.theory.view.UserActivity
import com.twt.service.theory.view.setDetail
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_activity_exam_detail.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_common_toolbar.view.*
import kotlinx.android.synthetic.main.theory_dialog_exam.*
import kotlinx.android.synthetic.main.theory_dialog_exam.view.*
import kotlinx.android.synthetic.main.theory_popupwindow_layout.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip
import com.twt.service.theory.model.TheoryApi
import org.jetbrains.anko.custom.async
import java.lang.Exception

class ExamDetailActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        recyclerView = findViewById(R.id.exam_detail_rec)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            setDetail("考试名称", intent.extras.getString("name"))
            setDetail("考试时间", "${intent.extras.getInt("duration")}分钟")
            setDetail("历史最高分", intent.extras.getString("score"))
            setDetail("通过状态", intent.extras.getString("status"))
            setDetail("有效次数", intent.extras.getString("test_time"))
            setDetail("剩余考试次数", (intent.extras.getString("test_time").toInt() - intent.extras.getString("tested_time").toInt()).toString())
        }
    }

    private fun initView() {
        setContentView(R.layout.theory_activity_exam_detail)
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        theory_person_profile.visibility = View.GONE
        theory_search.visibility = View.GONE
        enableLightStatusBarMode(true)
        theory_back.setOnClickListener {
            finish()
        }
        theory_exam_detail_button.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_dialog_exam, null, false)
            popupWindow.apply {
                isFocusable = true
                contentView = view
                if (intent.extras.getString("test_time").toInt() - intent.extras.getString("tested_time").toInt() == 0) {
                    contentView.theory_enter_word.text = "考试剩余次数为0!"
                }
                width = WindowManager.LayoutParams.MATCH_PARENT
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_exam_detail, null),
                        Gravity.CENTER,
                        0,
                        0
                )
                contentView.theory_enter_cancel.setOnClickListener {
                    popupWindow.dismiss()
                }
                contentView.theory_enter_comfirm.setOnClickListener {
                    if (intent.extras.getString("test_time").toInt() - intent.extras.getString("tested_time").toInt() == 0) {
                        popupWindow.dismiss()
                    } else {
                        val intent = Intent(contentView.context, AnswerActivity::class.java)
                        intent.putExtra("id", this@ExamDetailActivity.intent.extras.getInt("id"))
                        startActivity(intent)
                        popupWindow.dismiss()
                        finish()
                    }
                }
            }
        }
        theory_user_actionbar.title.text = "考试详情"
    }
}
