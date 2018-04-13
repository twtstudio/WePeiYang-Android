package com.twt.service.schedule2.view.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Course

class CourseDetailBottomFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG_SHARE_BS_DIALOG_FRAGMENT = "CourseDetailBottomFragment"
        private val cacheFragment = CourseDetailBottomFragment()

        fun showCourseDetailBottomSheet(activity: AppCompatActivity, course: Course) {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager
                    .findFragmentByTag(TAG_SHARE_BS_DIALOG_FRAGMENT) as? CourseDetailBottomFragment
            if (fragment == null) {
                fragment = cacheFragment
            }
            fragment.course = course
            fragment.show(fragmentManager, TAG_SHARE_BS_DIALOG_FRAGMENT)
        }
    }

    lateinit var course: Course

    override fun setupDialog(dialog: Dialog?, style: Int) {
        if (dialog == null) return

        val view: View = LayoutInflater.from(context).inflate(R.layout.schedule_frag_bottom_sheet, null)
        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)


        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_sheet)
        val layoutManager = LinearLayoutManager(context)
        val adapter = CourseDetailAdapter(context!!)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.refreshDataList(createCourseDetailList(course))
        recyclerView.post {
            // post方法才可以保证child被测量 否则拿到的都是0
            val arrangeSize = course.arrangeBackup.size
            val totalCollapsedCount = 2 + arrangeSize
            val childCount = layoutManager.childCount
            var totalCollapsedHeight = 0
            if (childCount > totalCollapsedCount) {
                for (i in 0 until totalCollapsedCount) {
                    totalCollapsedHeight += layoutManager.getChildAt(i).height
                }
            }
            behavior.peekHeight = totalCollapsedHeight
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }


}