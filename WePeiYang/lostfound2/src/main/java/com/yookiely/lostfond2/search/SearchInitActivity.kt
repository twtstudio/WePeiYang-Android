package com.yookiely.lostfond2.search

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import org.jetbrains.anko.db.*


class SearchInitActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var imageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var imageViewClean: ImageView
    private lateinit var editText: EditText
    private lateinit var imageViewBack: ImageView
    private var historyRecordData: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//隐藏actionbar，需在setContentView前面
        setContentView(R.layout.lf2_activity_search_init)
        toolbar = findViewById(R.id.search_init_toolbar)
        imageView = findViewById(R.id.lf2_search_icon_right)
        recyclerView = findViewById(R.id.lf2_search_hr_rv)
        imageViewClean = findViewById(R.id.lf2_search_clean)
        editText = findViewById(R.id.lf2_search_et)
        imageViewBack = findViewById(R.id.lf2_search_back)

//        database.use {
//            createTable(Utils.TABLE_NAME, true,
//                    Pair(Utils.ID, INTEGER + PRIMARY_KEY + AUTOINCREMENT),
//                    Pair(Utils.CONTENT, TEXT))
//        }
//        //处理原始数据
//        db = database.writableDatabase

        initRV()//初始化搜索历史的rv

        imageView.setOnClickListener {
            //点击放大镜，开始搜索
            val query = editText.text.toString()
            if (query != "") {
                //database(query,db)//处理搜索历史的数据库
                //并把搜索输入的内容传给展示搜索结果的activity
                submit(query)
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString("query", query)
                intent.putExtras(bundle)
                intent.setClass(this@SearchInitActivity, SearchActivity::class.java)
                ContextCompat.startActivity(this@SearchInitActivity, intent, bundle)
            }
        }

        imageViewClean.setOnClickListener {
            //            //清空搜索记录
//            val cursor = db.query("myTable", null,null,null,null,null,null)//cursor为游标
//            while (cursor.moveToNext()){
//                val number = cursor.getColumnIndex("content")
//                val content = cursor.getString(number)
//                db.delete("myTable","content=?", arrayOf(content))
//            }
            Hawk.put<MutableList<String>>("lf_search", mutableListOf())
            getdatas()
        }

        imageViewBack.setOnClickListener { onBackPressed() }//返回
    }

    //   private fun getdata(){
    //从数据库中取出历史记录的数据
//        val cursor = db.query("myTable", null, null, null, null, null, null)//cursor为游标
//        if (cursor!=null) {
//            historyRecordData = cursor.parseList(object : RowParser<String> {
//                override fun parseRow(columns: Array<Any?>): String {
//                    return columns[cursor.getColumnIndex("content")] as String
//                }
//            }).reversed()
//            cursor.close()
//        }else{
//            historyRecordData = mutableListOf()
//        }
    //      adapter.notifyDataSetChanged()
    //  }

    override fun onRestart() {
        super.onRestart()
        getdatas()
        //从搜索结果界面回到搜索界面
    }

    private fun initRV() {
        adapter = SearchAdapter(historyRecordData, this)
        adapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        getdatas()
    }

    private fun submit(query: String) {
        var temp = mutableListOf<String>()
        if (Hawk.get<MutableList<String>>("lf_search") != null) {
            temp = Hawk.get<MutableList<String>>("lf_search")
            temp.remove(query)
        }
        temp.add(query)
        if (temp.size > 5) {
            temp.removeAt(0)
        }
        Hawk.put("lf_search", temp)
        getdatas()
    }

    private fun getdatas() {
        //获取搜索历史的数据
        //只要每次企图更改hawk内存储的数据，就会调用改方法
        if (Hawk.get<MutableList<String>>("lf_search") != null) {
            historyRecordData.clear()
            historyRecordData.addAll((Hawk.get<MutableList<String>>("lf_search") as MutableList<String>))
            historyRecordData.reverse()
        }
        adapter.notifyDataSetChanged()
    }

    private fun database(query: String, db: SQLiteDatabase) {
        //对搜索历史的数据库处理
        var cursor = db.query("myTable", null, null, null, null, null, null)//cursor为游标

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val number = cursor.getColumnIndex("content")

                if (cursor.getString(number) == query) {
                    db.delete("myTable", "content=?", arrayOf(query))
                }
            }
        }

        val values = ContentValues()
        values.put("content", query)
        //times为存入的数据条数
        db.insert("myTable", null, values) //插入数据
        cursor = db.query("myTable", null, null, null, null, null, null)//cursor为游标

        val times = cursor.count

        if (times > 5) {
            //超过5条，则删掉最后一条
            cursor.moveToFirst()
            val timeOfID = cursor.getColumnIndex("_id")
            val minID = cursor.getInt(timeOfID)
            db.delete("myTable", "_id = {userID}", "userID" to minID)//arrayOf(min.toString()))
        }

        cursor.close()
    }
}