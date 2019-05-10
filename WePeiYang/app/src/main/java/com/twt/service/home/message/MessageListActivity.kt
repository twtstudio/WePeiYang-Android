package com.twt.service.home.message

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.withItems

class MessageListActivity : AppCompatActivity() {
    var items = mutableListOf<Item>()
    private lateinit var arrowBackIv: ImageView
    private lateinit var orderIv :ImageView
    lateinit var recyclerView: RecyclerView
    var numHistory : Int= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        arrowBackIv =findViewById(R.id.message_back_arrow)
        recyclerView = findViewById(R.id.message_rec)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getRecordMessage(CommonPreferences.studentid){ refreshState, recordMessage->
            when(refreshState){
                is RefreshState.Success -> {
                    numHistory = recordMessage!!.info.size
                    if(numHistory > 0){
                        items.add(AllKnowItem{
                            changeId(recordMessage.info)
                            items.clear()
                            recyclerView.withItems(items)
                        })
                    }
                    Log.d("message_record", recordMessage.info.toString())
                    recordMessage.info.forEach{
                        if(it.read == 0 ){
                            items.add(MessageListItem(this,it){item,_->
                                putId(it.id){ str ->
                                    Log.d("message_read",str)
                                }
                                items.remove(item)
                                recyclerView.withItems(items)
                            })
                        }
                    }
                    recyclerView.withItems(items)
                }
                is RefreshState.Failure -> {
                    Log.d("message_record2","出了点小问题")
                }
            }
        }
        arrowBackIv.setOnClickListener {
            onBackPressed()
        }
    }
    private fun changeId( info:List<Info>){
        info.forEach { Info ->
            putId(Info.id){
                Log.d("message_all_read",it)
            }
        }
    }
}
