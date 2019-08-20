package anim

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.service.theory.R
import com.twt.service.theory.view.AnswerActivity
import com.twt.service.theory.view.UserActivity
import com.twt.service.theory.view.setDetail
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_activity_exam_detail.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_common_toolbar.view.*

class ExamDetailActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_activity_exam_detail)
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableLightStatusBarMode(true)
        theory_person_profile.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        theory_back.setOnClickListener {
            finish()
        }
        theory_exam_detail_button.setOnClickListener {
            val intent = Intent(this, AnswerActivity::class.java)
            startActivity(intent)
        }


        theory_user_actionbar.title.text = "考试详情"

        recyclerView = findViewById(R.id.exam_detail_rec)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            setDetail("考试名称","中二病毕业考试")
            setDetail("考试时间","高中毕业")
            setDetail("历史最高分","99")
            setDetail("通过状态","通过")
            setDetail("有效次数","2")
            setDetail("剩余考试次数","8")
        }
    }
}
