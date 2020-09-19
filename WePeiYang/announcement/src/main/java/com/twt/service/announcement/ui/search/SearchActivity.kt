package com.twt.service.announcement.ui.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var backBotton: ImageView
    private lateinit var searchHistoryRec: RecyclerView
    private lateinit var editText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var problemText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        backBotton = findViewById(R.id.search_back)
        searchButton = findViewById(R.id.search_icon)
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
        }?.apply {
            historyItems.add(DeleteHistoryItem { clearHistory() })
        }
        searchHistoryRec.withItems {
            historyItems
        }

        backBotton.setOnClickListener {
            onBackPressed()
        }

        editText.apply {
            this.setOnClickListener {
                searchResultRec.visibility = View.INVISIBLE
                searchHistoryRec.visibility = View.VISIBLE
            }

            this.setOnEditorActionListener { _, actionId, event ->
                var flag = true
                if (actionId == EditorInfo.IME_ACTION_SEND || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    search(editText.text.trim().toString())
                } else {
                    flag = false
                }
                flag
            }
        }

        searchButton.setOnClickListener {
            search(editText.text.trim().toString())
        }

        editText.requestFocus()
    }

    private fun search(text: String) {
        if (text != "") {
            val temp = AnnoPreference.searchHistory
            temp.remove(text)
            temp.add(text)
            searchResultRec.visibility = View.INVISIBLE
            AnnoPreference.searchHistory = temp
            historyItems = AnnoPreference.searchHistory
                    .reversed()
                    .asSequence()
                    .map { it -> SearchHistoryItem(it) { search(it) } }
                    .toMutableList()
            if (AnnoPreference.searchHistory.size != 0) {
                historyItems.add(DeleteHistoryItem {
                    clearHistory()
                })
            }
            searchHistoryRecManager.refreshAll(historyItems)
            editText.setText("")

            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                val data = AnnoService.getQuestion(mapOf("search_string" to text)).awaitAndHandle {
                    it.printStackTrace()
                }?.data
                data?.let { ques ->
                    when (ques.size) {
                        0 -> {
                            searchHistoryRec.visibility = View.INVISIBLE
                            problemText.text = "未检索到相关问题"
                            problemText.visibility = View.VISIBLE
                        }
                        else -> {
                            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken, 0)
                            editText.clearFocus()
                            searchHistoryRec.visibility = View.INVISIBLE
                            searchResultRec.visibility = View.VISIBLE
                            searchResultRec.withItems(ques.map {
                                QuestionItem(it) {
                                    //详情页跳转
                                    Log.d("jumptodetail", "跳转详情页")
                                }
                            })
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