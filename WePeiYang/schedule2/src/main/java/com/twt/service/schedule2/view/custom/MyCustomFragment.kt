package com.twt.service.schedule2.view.custom

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.custom.CustomCourseManager
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.alert


class MyCustomFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.schedule_frag_my_custom, container, false)
        val itemManager = ItemManager()
        val mContext = activity
        val recyclerView = view.findViewById<RecyclerView>(R.id.my_custom_rec)
        val text = view.findViewById<TextView>(R.id.no_custom_text)
        text.apply {
            setTextColor(getColorCompat(R.color.colorPrimary))
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            itemAnimator = DefaultItemAnimator()
            backgroundColor = Color.parseColor("#F5F5F5")
            adapter = ItemAdapter(itemManager)
        }

        CustomCourseManager.getAllCustomCoureseLiveData().bindNonNull(this@MyCustomFragment) {
            itemManager.autoRefresh {
                removeAll { it is CustomCourseItem }

                if (it.isNotEmpty()) {
                    mtaExpose("schedule_自定义课程_用户自定义课程列表不为空_${it.size}")
                    text.visibility = View.GONE
                } else {
                    mtaExpose("schedule_自定义课程_用户自定义课程列表为空")
                    text.visibility = View.VISIBLE

                }

                it.forEach { customCourse ->
                    setCustomCourseItem(customCourse) {
                        alert {
                            title = "删除自定义课程"
                            message = "是否删除该自定义课程（所有时段）：${customCourse.name}"
                            positiveButton("删除自定义课程") {
                                launch(UI + QuietCoroutineExceptionHandler) {
                                    CustomCourseManager.deleteCustomCourse(customCourse)
                                    Toast.makeText(mContext, "${customCourse.name}删除成功", Toast.LENGTH_SHORT).show()
//                                    Toasty.info(mContext, "${customCourse.name}删除成功").show()
                                    CustomCourseManager.refreshCustomClasstable()
                                }
                            }
                        }.show()
                    }
                }
            }
        }
     return view
    }
}