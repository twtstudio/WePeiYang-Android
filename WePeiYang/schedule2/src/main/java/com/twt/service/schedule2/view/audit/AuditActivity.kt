package com.twt.service.schedule2.view.audit

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
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
import com.otaliastudios.autocomplete.RecyclerViewPresenter
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.model.audit.auditPopluarLiveData
import com.twt.service.schedule2.view.adapter.*
import com.twt.service.schedule2.view.audit.search.AnimationUtil
import com.twt.service.schedule2.view.custom.SingleTextItem
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor


class AuditActivity : CAppCompatActivity() {
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
                                Toasty.success(this@AuditActivity, "${viewmodel.course}").show()
                            }
                            textConfig = {
                                textColor = Color.BLACK
                                textSize = 16f
                            }
                        }
                    }

                    buttonItem("展开热门课程") {
                        textColor = getColorCompat(R.color.colorPrimary)
                        textSize = 14f
                        // 设置无边框
                        val typedValue = TypedValue()
                        this@AuditActivity.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
                        setBackgroundResource(typedValue.resourceId)

                        setOnClickListener {
                            if (popularCount != 10) {
                                popularCount = 10
                                text = "收缩热门课程"
                            } else {
                                popularCount = 3
                                text = "展开热门课程"
                            }
                            auditPopluarLiveData.refresh(CacheIndicator.LOCAL)
                        }
                    }

                    spaceItem("pop_below", 16) {
                        backgroundColor = Color.parseColor("#E5E5E5")
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

                    singleText("我的蹭课", titleTextBuilder)
                    it.forEach {
                        auditCourseItem(it)
                        if (it.infos.size > 1) {
                            for (i in 1 until it.infos.size) {
                                val additional = it.copy(infos = listOf(it.infos[i]))
                                auditCourseItem(additional)
                            }
                        }
                    }

                }
            }
        }, 20L) // 数据库总是比蹭热门快... 那就等等吧


        refreshData()
    }

    private fun setUpSearchView(editText: EditText) {
        editText.setOnEditorActionListener { v, actionId, event ->
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
        async(CommonPool + QuietCoroutineExceptionHandler) {
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

    class AutoCompletePresenter(context: Context, private val lifecycleOwner: LifecycleOwner, private val editText: EditText) : RecyclerViewPresenter<String>(context) {
        private val itemManager = ItemManager()
        private val adapter = ItemAdapter(itemManager)
        override fun instantiateAdapter(): RecyclerView.Adapter<*> {
            val liveData = AuditCourseManager.getSearchSuggestions("null")
            liveData.bindNonNull(lifecycleOwner) {
                //                Log.e("Auto", it.toString())
                /**
                 * 非空情况下再做自动提示的刷新
                 * 因为传来的数据是纯字符串 所以需要加些东西 前缀‘-’表示indicator 后缀‘college’表示学院
                 */
                if (it.isNotEmpty()) {
                    itemManager.refreshAll {
                        it.forEach {
                            when {
                                it.startsWith("-") -> indicatorText(it.removePrefix("-"))
                                it.endsWith("college") -> {
                                    val realText = it.removeSuffix("college")
                                    singleText(realText) {
                                        (parent as? View)?.setOnClickListener {
                                            editText.setText("#$realText") // 完善自动学院搜索
                                        }
                                    }
                                }
                                else -> {
                                    val realText = it.trim()
                                    singleText(realText) {
                                        (parent as? View)?.setOnClickListener {
                                            editText.setText(realText)
                                            editText.onEditorAction(EditorInfo.IME_ACTION_SEARCH) // 触发搜索事件
                                            editText.setText("") //清空
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return adapter
        }

        override fun onQuery(query: CharSequence?) {
            if (query.isNullOrBlank() || query == "null") showHelpMessage()
            AuditCourseManager.getSearchSuggestions(query.toString())
        }

        override fun getPopupDimensions(): PopupDimensions {
            val dimensions = super.getPopupDimensions()
            dimensions.width = matchParent
            return dimensions
        }

        private fun showHelpMessage() {
            itemManager.refreshAll {
                indicatorText("提示")
                singleText("") {
                    text = "输入 ‘#’ 来获取学院提示\n输入 '#'+学院名 来获取学院课程\n点击学院名可获取该学院课程\n点击课程名可查询该课程"
                }
            }
        }

    }

}