package xyz.rickygao.gpa2.view

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageButton
import android.widget.RatingBar
import com.twt.wepeiyang.commons.experimental.extensions.consume
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.extensions.fitSystemWindowWithNavigationBar
import com.twt.wepeiyang.commons.experimental.extensions.fitSystemWindowWithStatusBar
import es.dmoral.toasty.Toasty
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.api.Evaluate
import xyz.rickygao.gpa2.api.GpaProvider

/**
 * Created by rickygao on 2018/1/23.
 */

class EvaluateActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var backBtn: ImageButton
    private lateinit var okBtn: ImageButton

    private lateinit var containerCl: ConstraintLayout

    private lateinit var q1Rb: RatingBar
    private lateinit var q2Rb: RatingBar
    private lateinit var q3Rb: RatingBar
    private lateinit var q4Rb: RatingBar
    private lateinit var q5Rb: RatingBar
    private lateinit var noteEt: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gpa2_activity_evaluate)

        val evaluate = intent.getParcelableExtra<Evaluate>("evaluate")

        enableLightStatusBarMode(true)

        // init toolbar
        toolbar = findViewById<Toolbar>(R.id.toolbar).also {
            fitSystemWindowWithStatusBar(it)
            setSupportActionBar(it)
        }
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        backBtn = findViewById<ImageButton>(R.id.btn_back).apply {
            setOnClickListener {
                onBackPressed()
            }
        }

        okBtn = findViewById<ImageButton>(R.id.btn_ok).apply {
            setOnClickListener {
                GpaProvider.postEvaluate(evaluate, q1Rb.rating.toInt(), q2Rb.rating.toInt(),
                        q3Rb.rating.toInt(), q4Rb.rating.toInt(), q5Rb.rating.toInt(),
                        noteEt.text.toString())
            }
        }

        // init container
        containerCl = findViewById<ConstraintLayout>(R.id.cl_container)
                .also(this::fitSystemWindowWithNavigationBar)

        q1Rb = findViewById(R.id.rb_q1)
        q2Rb = findViewById(R.id.rb_q2)
        q3Rb = findViewById(R.id.rb_q3)
        q4Rb = findViewById(R.id.rb_q4)
        q5Rb = findViewById(R.id.rb_q5)
        noteEt = findViewById(R.id.et_note)

        GpaProvider.successLiveData.consume(this) {
            Toasty.success(this, it).show()
            finish()
        }

        GpaProvider.errorLiveData.consume(this) {
            Toasty.error(this, it).show()
        }
    }

}