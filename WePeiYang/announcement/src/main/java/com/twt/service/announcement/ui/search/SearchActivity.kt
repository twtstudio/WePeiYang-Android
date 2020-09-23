package com.twt.service.announcement.ui.search

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.ui.main.QuestionItem
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private var historyItems = mutableListOf<Item>()
    private val searchHistoryRecManager by lazy { ItemManager() }
    private val searchResultRecManager by lazy { ItemManager() }
    private lateinit var searchResultRec: RecyclerView
    private lateinit var searchHistoryRec: RecyclerView
    private lateinit var editText: EditText
    private lateinit var problemText: TextView


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<ImageView>(R.id.search_back).apply {
            setOnClickListener { onBackPressed() }
        }

        findViewById<ImageView>(R.id.refresh_icon).apply {
            setOnClickListener {
                search(editText.text.trim().toString())
            }
        }
        editText = findViewById(R.id.search_edit)
        problemText = findViewById(R.id.search_problem_text)

        searchResultRec = findViewById<RecyclerView>(R.id.anno_search_result_rv).apply {
            adapter = ItemAdapter(searchResultRecManager)
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        searchHistoryRec = findViewById<RecyclerView>(R.id.anno_search_history_rv).apply {
            this.adapter = ItemAdapter(searchHistoryRecManager)
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        AnnoPreference.searchHistory.reversed().mapTo(historyItems) { t ->
            SearchHistoryItem(t) { search(t) }
        }.takeIf {
            AnnoPreference.searchHistory.size > 0
        }?.also {
            historyItems.add(DeleteHistoryItem { clearHistory() })
        }


        editText.apply {
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    GlobalScope.launch(Dispatchers.Main) {
                        problemText.visibility = View.INVISIBLE
                        searchResultRec.visibility = View.INVISIBLE
                        searchHistoryRec.visibility = View.VISIBLE
                        //这里不能用refreshAll  会出错
                        searchHistoryRecManager.clear()
                        searchHistoryRecManager.addAll(historyItems)
                    }
                }
                false
            }

            setOnEditorActionListener { _, actionId, event ->
                var flag = true
                if (actionId == EditorInfo.IME_ACTION_SEND || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    search(editText.text.trim().toString())
                } else {
                    flag = false
                }
                flag
            }
        }



        editText.requestFocus()
        searchHistoryRec.withItems(historyItems)

    }

    private fun search(text: String) {
        if (text != "") {
            val temp = AnnoPreference.searchHistory
            temp.remove(text)
            temp.add(text)
            AnnoPreference.searchHistory = temp
            AnnoPreference.searchHistory.reversed().mapTo(historyItems) { t ->
                SearchHistoryItem(t) { search(t) }
            }.takeIf {
                AnnoPreference.searchHistory.size > 0
            }?.also {
                historyItems.add(DeleteHistoryItem { clearHistory() })
            }
            searchHistoryRec.visibility = View.INVISIBLE
            editText.setText(text)
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken, 0)
            editText.clearFocus()

            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                val data = AnnoService.getQuestion(mapOf("search_string" to text)).awaitAndHandle {
                    Log.d("announce:load failed", it.message)
                    searchResultRec.visibility = View.INVISIBLE
                    problemText.visibility = View.VISIBLE
                    problemText.text = "网络好像出了点问题"
                }?.data
                data?.let { ques ->
                    when (ques.size) {
                        0 -> {
                            problemText.visibility = View.VISIBLE
                            problemText.text = "未检索到相关问题"
                            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken, 0)
                            editText.clearFocus()
                        }
                        else -> {
                            problemText.visibility = View.INVISIBLE
                            searchResultRec.visibility = View.VISIBLE
                            searchResultRecManager.refreshAll(ques.map {
                                QuestionItem(it) {
                                    //详情页跳转
                                    Log.d("jumptodetail", "跳转详情页")
                                }
                            })
//                            Toast.makeText(this@SearchActivity,"获取数据成功",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun clearHistory() {
        historyItems.clear()
        AnnoPreference.searchHistory.clear()
        searchHistoryRec.withItems(historyItems)
    }


}