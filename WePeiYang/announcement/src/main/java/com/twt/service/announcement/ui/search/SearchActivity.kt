package com.twt.service.announcement.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
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
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.ui.detail.DetailActivity
import com.twt.service.announcement.ui.main.ButtonItem
import com.twt.service.announcement.ui.main.QuestionItem
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.withItems
import jp.wasabeef.recyclerview.animators.FadeInAnimator
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
    private lateinit var resultLayoutManager: LinearLayoutManager
    private var nextUrl: String? = null
    private val user_id = 1

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
                search(this@SearchActivity, editText.text.trim().toString())
            }
        }
        editText = findViewById(R.id.search_edit)
        problemText = findViewById(R.id.search_problem_text)

        searchResultRec = findViewById<RecyclerView>(R.id.anno_search_result_rv).apply {
            adapter = ItemAdapter(searchResultRecManager)
            layoutManager = LinearLayoutManager(this@SearchActivity)

            itemAnimator = FadeInAnimator()
            itemAnimator?.let {
                it.addDuration = 100
                it.removeDuration = 400
                it.changeDuration = 300
                it.moveDuration = 200
            }

            resultLayoutManager = layoutManager as LinearLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && resultLayoutManager.findLastVisibleItemPosition() == searchResultRecManager.itemListSnapshot.size - 1) {
                        nextUrl?.let { url ->
                            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                url.split("page=")[1].toIntOrNull()?.let { page ->
                                    AnnoService.getQuestion(
                                            mapOf("searchString" to editText.text,
                                                    "tagList" to emptyList<Int>(),
                                                    "limits" to 20,
                                                    "page" to page, "user_id" to user_id)
                                    ).awaitAndHandle {
                                        it.printStackTrace()
                                    }?.data
                                }?.apply {
                                    nextUrl = if (to != total) next_page_url else null
                                }?.data?.let { next ->
                                    takeIf { next.isNotEmpty() }?.apply {
                                        val items = next.map { ques ->
                                            QuestionItem(context, ques, onClick = {
                                                // 问题详情跳转
                                                val mIntent: Intent = Intent(this@SearchActivity, DetailActivity::class.java)
                                                        .putExtra("question", ques)
                                                startActivity(mIntent)
                                            })
                                        }
                                        searchResultRec.visibility = View.VISIBLE
                                        with(searchResultRecManager) {
                                            removeAt(searchResultRecManager.itemListSnapshot.size - 1)
                                            addAll(items)
                                            add(ButtonItem())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })


        }

        searchHistoryRec = findViewById<RecyclerView>(R.id.anno_search_history_rv).apply {
            this.adapter = ItemAdapter(searchHistoryRecManager)
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        refreshHistory()

        editText.apply {
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    GlobalScope.launch(Dispatchers.Main) {
                        problemText.visibility = View.INVISIBLE
                        searchResultRec.visibility = View.INVISIBLE
                        searchHistoryRec.visibility = View.VISIBLE
                        //这里不能用refreshAll  会出错
                        Log.d("whynohistory", "whalkdsjfljdsafl")
                        refreshHistory()
                        searchHistoryRecManager.clear()
                        searchHistoryRecManager.addAll(historyItems)
                    }
                }
                false
            }

            setOnEditorActionListener { _, actionId, event ->
                var flag = true
                if (actionId == EditorInfo.IME_ACTION_SEND || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    search(this@SearchActivity, editText.text.trim().toString())
                } else {
                    flag = false
                }
                flag
            }
        }



        editText.requestFocus()
        searchHistoryRec.withItems(historyItems)

    }

    private fun refreshHistory() {
        AnnoPreference.searchHistory.reversed().mapTo(historyItems) { t ->
            SearchHistoryItem(t) { search(this, t) }
        }.takeIf {
            AnnoPreference.searchHistory.size > 0
        }?.also {
            historyItems.add(DeleteHistoryItem { clearHistory() })
        }
    }

    private fun search(context: Context, text: String) {
        if (text != "") {
            val temp = AnnoPreference.searchHistory
            temp.remove(text)
            temp.add(text)
            AnnoPreference.searchHistory = temp
            AnnoPreference.searchHistory.reversed().mapTo(historyItems) { t ->
                SearchHistoryItem(t) { search(context, t) }
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
                AnnoService.getQuestion(mapOf("searchString" to text, "tagList" to emptyList<Int>(), "limits" to 20, "page" to 1, "user_id" to user_id)).awaitAndHandle {
                    Log.d("announce:load failed", it.message)
                    searchResultRec.visibility = View.INVISIBLE
                    problemText.visibility = View.VISIBLE
                    problemText.text = "网络好像出了点问题"
                }?.data?.apply {
                    nextUrl = if (to != total) next_page_url else null
                }?.data?.let { ques ->
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
                                QuestionItem(context, it, onClick = {
                                    //详情页跳转
                                    val mIntent: Intent = Intent(this@SearchActivity, DetailActivity::class.java)
                                            .putExtra("question", it)
                                    startActivity(mIntent)
                                })
                            })
                            searchResultRecManager.add(ButtonItem())
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