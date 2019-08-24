package com.twt.service.schedule2.view.audit.search

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.otaliastudios.autocomplete.Autocomplete
import com.otaliastudios.autocomplete.AutocompletePolicy
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.ScheduleDb
import com.twt.service.schedule2.model.audit.*
import com.twt.service.schedule2.view.adapter.indicatorText
import com.twt.service.schedule2.view.audit.AutoCompletePresenter
import com.twt.service.schedule2.view.audit.auditCourseItem
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.coroutines.*
import org.jetbrains.anko.alert

class SearchResultActivity : CAppCompatActivity() {
    /*调用以打开searchResultActivity*/
    companion object {
        val KEY_COURSE_NAME = "CourseName"
        fun searchCourse(context: Context, courseName: String) {
            val searchIntent = Intent(context, SearchResultActivity::class.java)
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            searchIntent.putExtra(SearchResultActivity.KEY_COURSE_NAME, courseName)
            context.startActivity(searchIntent)
        }
    }

    lateinit var searchContainer: ViewGroup
    lateinit var searchEditText: EditText
    lateinit var autocomplete: Autocomplete<String>
    val itemManager = ItemManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_audit)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        /**
         * 隐藏软键盘
         */
        fun hideSoftInput() =
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        val titleText: TextView = findViewById(R.id.tv_toolbar_title)
        titleText.apply {
            text = "蹭课搜索"
        }

        val refreshImg: ImageView = findViewById(R.id.iv_toolbar_refresh)
        refreshImg.apply {
            visibility = View.GONE
        }
        val addImg: ImageView = findViewById(R.id.iv_toolbar_add)
        addImg.visibility = View.GONE

        searchContainer = findViewById(R.id.search_container)

        val searchReturnImg: ImageView = findViewById(R.id.img_search_back)
        searchReturnImg.setOnClickListener {
            hideSoftInput()
            searchContainer.visibility = View.INVISIBLE
        }

        searchEditText = findViewById(R.id.et_search)
        setUpSearchView(searchEditText)

        val recyclerView: RecyclerView = findViewById(R.id.rec_audit)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = ItemAdapter(itemManager)
        }

        val intent: Intent? = getIntent()
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val courseName = intent?.getStringExtra(KEY_COURSE_NAME)
        searchCourse(courseName)
    }

    private fun searchCourse(courseName: String?) {
        val exceptionHandler = CoroutineExceptionHandler({ _, throwable ->
            itemManager.refreshAll {
                throwable.printStackTrace()
                indicatorText("出现错误")
                singleText("出现了些错误，点击重试") {
                    (parent as View).setOnClickListener {
                        searchCourse(courseName)
                    }
                }
            }
        })
        /**
         * 蹭课搜索以及搜索的错误处理 / 还没有验证
         */
        courseName?.apply {
            GlobalScope.launch(exceptionHandler + Dispatchers.Main) {
                val result = AuditApi.searchCourse(courseName).awaitAndHandle { it.printStackTrace() }?.data
                        ?: throw IllegalStateException("蹭课查询失败")
                itemManager.refreshAll {

//                    indicatorText("我们为可爱的你找到${result.size}门课程，共${result.flatMap { it.info }.count()}个上课时间")
                    var invalidCount = 0
                    var count = 0
                    result.map(AuditSearchCourse::convertToAuditCourse).forEach { auditCourse: AuditCourse ->
                        /* 办公网的返回数据有问题，所以只能先这样手动判断 */
                        val course = auditCourse.convertToCourse()
                        if (course.arrange[0].room == "楼" || auditCourse.infos[0].teacher == "") {
                            invalidCount += 1
                        } else {
                            auditCourseItem(auditCourse) {
                                showAuditDialog(auditCourse)
                            }
                            if (auditCourse.infos.size > 1) {
                                for (i in 1 until auditCourse.infos.size) {
                                    val additional = auditCourse.copy(infos = listOf(auditCourse.infos[i]))
                                    auditCourseItem(additional) {
                                        showAuditDialog(additional)
                                    }
                                }
                            }
                        }
                    }
                    if (invalidCount == result.size) {
                        singleText("无可用搜索结果")
                    }
                }
            }
        }
    }

    private fun showAuditDialog(auditCourse: AuditCourse) {
        alert {
            title = "大佬真的要蹭课吗？"
            GlobalScope.async(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                val duplicateList = withContext(Dispatchers.Default) {
                    ScheduleDb.auditCourseDao.loadAllAuditCourses().filter {
                        it.courseName == auditCourse.courseName
                    }
                }
                when {
                    duplicateList.isEmpty() -> {
                        message = "蹭课：${auditCourse.courseName}"
                        positiveButton("确认") {
                            GlobalScope.async(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                val auditResult = audit(auditCourse.courseId, auditCourse.infos[0].id.toString()).awaitAndHandle { it.printStackTrace() }?.message
                                        ?: "蹭课失败"
                                Toasty.success(this@SearchResultActivity, "$auditResult - ${auditCourse.courseName}").show()

                                async(Dispatchers.Default + QuietCoroutineExceptionHandler) {
                                    AuditCourseManager.refreshAuditClasstable()
                                }
                            }
                        }
                    }
                    duplicateList.any { it.courseId == auditCourse.courseId } -> {
                        message = "之前已有蹭${auditCourse.courseName}, 自动为您添加该时段（如果不重复）"
                        val coursesAlreadyAudit = duplicateList.filter { it.courseId == auditCourse.courseId }
                        var infoIds = ""
                        coursesAlreadyAudit.flatMap { it.infos }.forEach { infoIds += "${it.id}," }
                        infoIds += auditCourse.infos[0].id
                        positiveButton("确认") {
                            GlobalScope.async(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                val auditResult = audit(auditCourse.courseId, infoIds).awaitAndHandle { it.printStackTrace() }?.message
                                Toasty.success(this@SearchResultActivity, "$auditResult - ${auditCourse.courseName}").show()

                                async(Dispatchers.Default + QuietCoroutineExceptionHandler) {
                                    AuditCourseManager.refreshAuditClasstable()
                                }
                            }
                        }
                    }
                }
                negativeButton("不蹭了") {
                    it.dismiss()
                }
                show()
            }
        }
    }

    private fun audit(courseId: Int, infoIds: String) = AuditApi.audit(CommonPreferences.studentid, courseId, infoIds)


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchItem.setOnMenuItemClickListener {
            searchContainer.post {
                searchContainer.visibility = View.VISIBLE
                AnimationUtil.reveal(searchContainer, object : AnimationUtil.AnimationListener {
                    override fun onAnimationStart(view: View?): Boolean = true

                    override fun onAnimationEnd(view: View?): Boolean {
                        searchEditText.setText(null)
                        searchEditText.requestFocus()
                        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                        autocomplete.showPopup("null")
                        return true
                    }

                    override fun onAnimationCancel(view: View?): Boolean = true

                })
            }
        }

        val refreshItem = menu.findItem(R.id.action_refresh).apply {
            isVisible = false
        }

        return true
    }

    private fun setUpSearchView(editText: EditText) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editText.clearFocus();
                searchContainer.visibility = View.INVISIBLE
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(editText.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        AuditCourseManager.refreshAuditSearchDatabase()
        autocomplete = Autocomplete.on<String>(editText).with(object : AutocompletePolicy {
            override fun shouldShowPopup(text: Spannable?, cursorPos: Int): Boolean = (text?.length) ?: 0 > 0

            override fun shouldDismissPopup(text: Spannable?, cursorPos: Int): Boolean = text?.length == 0

            override fun getQuery(text: Spannable): CharSequence = text

            override fun onDismiss(text: Spannable?) {}
        }).with(ColorDrawable(Color.WHITE)).with(4f).with(AutoCompletePresenter(this, this, editText)).build()
    }

}