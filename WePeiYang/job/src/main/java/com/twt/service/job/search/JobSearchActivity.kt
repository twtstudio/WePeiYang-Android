package com.twt.service.job.search

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import com.orhanobut.hawk.Hawk
import com.twt.service.job.service.*
import com.twt.service.job.R

class JobSearchActivity : AppCompatActivity() {

    private lateinit var back: ImageView
    private lateinit var searchEditText: EditText // 搜索框
    private lateinit var searchImageView: ImageView // 搜索放大镜按钮键
    private val manager = supportFragmentManager
    private var ft = manager.beginTransaction()
    private var historyFragment: Fragment = JobSearchHistoryFragment()
    private var resultFragment: Fragment = JobSearchResultFragment()
    lateinit var keyword: String private set
    private lateinit var sh: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_activity_search)
        window.statusBarColor = resources.getColor(R.color.job_green)
        bindId()
        historyFragment = JobSearchHistoryFragment()
        ft.add(R.id.job_search_fl_fragment, historyFragment).commit()
        back.setOnClickListener { onBackPressed() }
        searchImageView.setOnClickListener {
            if (searchEditText.text.trim().toString() != "") {
                search(searchEditText.text.trim().toString())
            }
        }
        searchEditText.setOnEditorActionListener { _, actionId, event ->
            var flag = true
            if (actionId == EditorInfo.IME_ACTION_SEND || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (searchEditText.text.trim().toString() != "") {
                    search(searchEditText.text.trim().toString())
                }
            } else {
                flag = false
            }
            flag
        }
        if (Hawk.get<MutableList<String>>(SEARCH_HISTORY) == null) {
            Hawk.put<MutableList<String>>(SEARCH_HISTORY, mutableListOf())
        }
        sh = Hawk.get<MutableList<String>>(SEARCH_HISTORY)
    }

    private fun bindId() {
        back = findViewById(R.id.job_search_iv_back)
        searchEditText = findViewById(R.id.job_search_et_input)
        searchImageView = findViewById(R.id.job_search_iv_icon)
    }

    @SuppressLint("CommitTransaction")
    fun search(keyword: String) {
        this.keyword = keyword
        sh = Hawk.get(SEARCH_HISTORY)
        sh.remove(keyword)
        sh.add(keyword)
        if (sh.size > 10) {
            sh.remove(sh.first().toString())
        }
        Hawk.put(SEARCH_HISTORY, sh)
        if (searchEditText.text.trim().toString() != keyword) searchEditText.setText(keyword.toCharArray(), 0, keyword.length)
        ft = manager.beginTransaction()
        ft.remove(historyFragment)
        resultFragment = JobSearchResultFragment()
        ft.replace(R.id.job_search_fl_fragment, resultFragment).commit()
        searchEditText.apply {
            setOnClickListener { showHistory() }
            setSelection(searchEditText.text.length)
        }
    }

    @SuppressLint("CommitTransaction")
    private fun showHistory() {
        ft = manager.beginTransaction()
        ft.remove(resultFragment)
        historyFragment = JobSearchHistoryFragment()
        ft.replace(R.id.job_search_fl_fragment, historyFragment)
        ft.commit()
    }
}
