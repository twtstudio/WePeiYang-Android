package anim

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.orhanobut.hawk.Hawk
import com.twt.service.theory.R
import com.twt.service.theory.view.AnswerActivity
import com.twt.service.theory.view.AnswerManager
import com.twt.service.theory.view.setDetail
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.theory_activity_exam_detail.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_common_toolbar.view.*
import kotlinx.android.synthetic.main.theory_dialog_exam.view.*
import org.apache.thrift.server.AbstractNonblockingServer
import org.jetbrains.anko.dip

class ExamDetailActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.M)
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


    @RequiresApi(Build.VERSION_CODES.M)
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

            var lock = Hawk.get<Boolean>("theory_answer_lock", false)
            val nowTime = System.currentTimeMillis() / 1000//获取现在的时间

            if (lock) {//时间超时，清空保留的缓存
                AnswerManager.recover()
                if (nowTime - AnswerManager.getBeginTime() > AnswerManager.getDuration()) {
                    lock = false
                    Hawk.put("theory_answer_lock", false)
                    //这里提交试卷
                }
            }

            popupWindow.apply {
                isFocusable = true
                contentView = view
                if (intent.extras.getString("test_time").toInt() - intent.extras.getString("tested_time").toInt() == 0) {
                    contentView.theory_enter_word.text = "考试剩余次数为0!"
                }
                if (lock) {
                    contentView.theory_enter_word.text = "检测到未完成的考试，是否继续?"
                    Log.d("BUG", "zz")
                }
                width = dip(320)
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
                        intent.putExtra("lock", lock)//用来说明这个是从网络加载还是从缓存加载
                        intent.putExtra("duration", if (lock) (AnswerManager.getDuration() - (nowTime - AnswerManager.getBeginTime())).toInt() else this@ExamDetailActivity.intent.extras.getInt("duration") * 60)
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
