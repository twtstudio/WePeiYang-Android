package com.twt.service.schedule2.view.audit

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.otaliastudios.autocomplete.RecyclerViewPresenter
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.view.adapter.indicatorText
import com.twt.service.schedule2.view.audit.search.SearchResultActivity
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import org.jetbrains.anko.matchParent

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
                                        SearchResultActivity.searchCourse(context, realText)
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