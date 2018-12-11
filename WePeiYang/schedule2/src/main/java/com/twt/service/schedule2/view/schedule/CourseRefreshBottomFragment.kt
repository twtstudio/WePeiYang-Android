package com.twt.service.schedule2.view.schedule

import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.RefreshCallback
import com.twt.service.schedule2.view.detail.CourseDetailAdapter
import com.twt.wepeiyang.commons.mta.mtaClick

class CourseRefreshBottomFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG_SHARE_BS_DIALOG_FRAGMENT = "CourseRefreshBottomFragment"
        private val cacheFragment = CourseRefreshBottomFragment()

        fun showCourseDetailBottomSheet(activity: AppCompatActivity, refreshCallback: RefreshCallback = {}) {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager
                    .findFragmentByTag(TAG_SHARE_BS_DIALOG_FRAGMENT) as? CourseRefreshBottomFragment
            if (fragment == null) {
                fragment = cacheFragment
            }
            if (fragment.isAdded) return
            fragment.refreshCallback = refreshCallback
            fragment.show(fragmentManager, TAG_SHARE_BS_DIALOG_FRAGMENT)
            mtaClick("schedule_点击顶部刷新按钮")
        }
    }

    var refreshCallback: RefreshCallback = {}

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

        adapter.refreshDataList(createRefreshList(refreshCallback = refreshCallback))
        recyclerView.post {
            // post方法才可以保证child被测量 否则拿到的都是0
            val totalCollapsedCount = 2
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