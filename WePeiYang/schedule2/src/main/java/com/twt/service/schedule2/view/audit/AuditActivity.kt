package com.twt.service.schedule2.view.audit

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.util.TypedValue
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
import com.twt.service.schedule2.model.audit.AuditApi
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.model.audit.auditPopluarLiveData
import com.twt.service.schedule2.view.adapter.IconLabelItem
import com.twt.service.schedule2.view.adapter.SpaceItem
import com.twt.service.schedule2.view.adapter.iconLabel
import com.twt.service.schedule2.view.adapter.spaceItem
import com.twt.service.schedule2.view.audit.search.AnimationUtil
import com.twt.service.schedule2.view.audit.search.SearchResultActivity
import com.twt.service.schedule2.view.custom.SingleTextItem
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor


class AuditActivity : CAppCompatActivity() {
    /*为了实现在多节课程的底部栏进入自定义课程界面 */
    companion object {
        fun startAuditActivity(context: Context) {
            val auditIntent = Intent(context, AuditActivity::class.java)
            auditIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(auditIntent)
        }
    }

    lateinit var recyclerView: RecyclerView
    lateinit var viewGroup: ViewGroup
    lateinit var autocomplete: Autocomplete<String>
    lateinit var searchEditText: EditText
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_audit)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewGroup = findViewById(R.id.search_container)
//        viewGroup.visibility = View.GONE

        val searchReturnImg: ImageView = findViewById(R.id.img_search_back)
        searchReturnImg.setOnClickListener {
            viewGroup.visibility = View.INVISIBLE
        }
        searchEditText = findViewById(R.id.et_search)
        setUpSearchView(searchEditText)

        val titleText: TextView = findViewById(R.id.tv_toolbar_title)
        titleText.apply {
            text = "蹭课"
        }

        val refreshImg: ImageView = findViewById(R.id.iv_toolbar_refresh)
        refreshImg.apply {
            visibility = View.GONE
        }
        val addImg: ImageView = findViewById(R.id.iv_toolbar_add)
        addImg.visibility = View.GONE

        recyclerView = findViewById(R.id.rec_audit)
        val itemManager = ItemManager()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AuditActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = ItemAdapter(itemManager)
            backgroundColor = Color.parseColor("#F5F5F5")
        }

        val titleTextBuilder: TextView.() -> Unit = {
            textSize = 16f
            textColor = Color.parseColor("#60000000")
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }

        var popularCount = 3
        auditPopluarLiveData.bindNonNull(this) {
            itemManager.autoRefresh {
                removeAll { it is IconLabelItem }
                removeAll { it is SingleTextItem && it.text == "热门课程" }
                removeAll { it is SpaceItem && it.tag == "pop_below" }
                removeAll { it is ButtonItem }

                val needItems = mutableListOf<Item>().apply {
                    singleText("热门课程", builder = titleTextBuilder)
                    it.take(popularCount).forEach { viewmodel ->
                        iconLabel {
                            imgResId = R.drawable.ic_schedule_event
                            content = viewmodel.course.name
                            clickBlock = {
                                SearchResultActivity.searchCourse(this@AuditActivity, content)
                            }
                            textConfig = {
                                textColor = Color.BLACK
                                textSize = 16f
                            }
                        }
                    }

                    buttonItem("展开热门课程") {
                        fun refreshBtnState() {
                            if (popularCount != 10) {
                                popularCount = 10
                                text = "收缩热门课程"
                            } else {
                                popularCount = 3
                                text = "展开热门课程"
                            }
                        }
                        textColor = getColorCompat(R.color.colorPrimary)
                        textSize = 14f
                        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                        // 设置无边框
                        val typedValue = TypedValue()
                        this@AuditActivity.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
                        setBackgroundResource(typedValue.resourceId)

                        setOnClickListener {
                            refreshBtnState()
                            auditPopluarLiveData.refresh(CacheIndicator.LOCAL)
                        }


                    }

                    spaceItem("pop_below", 4) {
                    }

                }
                addAll(0, needItems)
            }
        }

        Handler(mainLooper).postDelayed({
            AuditCourseManager.getAuditListLive().bindNonNull(this@AuditActivity) {
                itemManager.autoRefresh {

                    removeAll { it is AuditCourseItem }
                    removeAll { it is SingleTextItem && it.text == "我的蹭课" }

                    if (it.isNotEmpty()) {
                        mtaExpose("schedule_蹭课_用户蹭课列表不为空_${it.size}")
                    } else {
                        mtaExpose("schedule_蹭课_用户蹭课列表为空")
                    }
                    singleText("我的蹭课", titleTextBuilder)
                    it.forEach { auditCourse ->
                        auditCourseItem(auditCourse) {
                            alert {
                                title = "取消蹭课"
                                message = "是否取消该课程蹭课（所有时段）：${auditCourse.courseName}"
                                positiveButton("取消蹭课") {
                                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                        val result = AuditApi.cancelAudit(CommonPreferences.studentid, auditCourse.courseId.toString()).awaitAndHandle { it.printStackTrace() }?.message
                                                ?: "出现错误"
                                        Toasty.info(this@AuditActivity, "${auditCourse.courseName} -> $result").show()
                                        refreshData()
                                    }
                                }
                            }.show()
                        }
                        if (auditCourse.infos.size > 1) {
                            for (i in 1 until auditCourse.infos.size) {
                                val additional = auditCourse.copy(infos = listOf(auditCourse.infos[i]))
                                auditCourseItem(additional) {
                                    alert {
                                        title = "取消蹭课"
                                        message = "是否取消该课程蹭课（所有时段）：${additional.courseName}"
                                        positiveButton("取消蹭课") {
                                            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                                val result = AuditApi.cancelAudit(CommonPreferences.studentid, additional.courseId.toString()).awaitAndHandle { it.printStackTrace() }?.message
                                                        ?: "出现错误"
                                                Toasty.info(this@AuditActivity, "${additional.courseName} -> $result").show()
                                                refreshData()
                                            }
                                        }
                                    }.show()
                                }
                            }
                        }
                    }

                }
            }
        }, 20L) // 数据库总是比蹭热门快... 那就等等吧


    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun setUpSearchView(editText: EditText) {
        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editText.clearFocus();
                viewGroup.visibility = View.INVISIBLE
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

    private fun refreshData() {
        GlobalScope.launch(Dispatchers.Default + QuietCoroutineExceptionHandler) {
            AuditCourseManager.refreshAuditClasstable()
            auditPopluarLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
            AuditCourseManager.refreshAuditSearchDatabase()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchItem.setOnMenuItemClickListener {
            viewGroup.post {
                viewGroup.visibility = View.VISIBLE
                AnimationUtil.reveal(viewGroup, object : AnimationUtil.AnimationListener {
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

        val refreshItem = menu.findItem(R.id.action_refresh)
        refreshItem.setOnMenuItemClickListener {
            refreshData()
            return@setOnMenuItemClickListener true
        }

        return true
    }


    override fun onBackPressed() {
        if (viewGroup.visibility != View.INVISIBLE) {
            viewGroup.visibility = View.INVISIBLE
        } else super.onBackPressed()
    }

}