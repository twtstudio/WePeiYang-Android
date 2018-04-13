package com.twt.service.schedule2.view.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Course
import android.support.design.widget.CoordinatorLayout
import android.widget.FrameLayout
import android.support.design.widget.BottomSheetDialog
import android.content.DialogInterface



/**
 * 这个是直接用在底部的 因为多门课程冲突的时候 需要用一个TabLayout
 */
@SuppressLint("ValidFragment")
class CourseDetailFragment(val course: Course) : Fragment() {

    var totalCollapsedHeight = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.schedule_frag_bottom_sheet, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_sheet)
        val layoutManager = LinearLayoutManager(context)
        val adapter = CourseDetailAdapter(context!!)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.refreshDataList(createCourseDetailList(course))

        view.rootView.viewTreeObserver.addOnGlobalLayoutListener {
            // post方法才可以保证child被测量 否则拿到的都是0
            val arrangeSize = course.arrangeBackup.size
            val totalCollapsedCount = 2 + arrangeSize
            val childCount = layoutManager.childCount
            if (childCount > totalCollapsedCount) {
                for (i in 0 until totalCollapsedCount) {
                    totalCollapsedHeight += layoutManager.getChildAt(i).height
                }
            }
        }

        return view
    }
}

class MultiCourseDetailFragment : BottomSheetDialogFragment() {
    private lateinit var adapter: FragmentPagerAdapter
    private val fragments = mutableListOf<CourseDetailFragment>()
    private val courses = mutableListOf<Course>()
    private var tabLayoutHeight = 0

    companion object {
        private const val TAG_SHARE_BS_DIALOG_FRAGMENT = "MultiCourseDetailFragment"
        private val cacheFragment = MultiCourseDetailFragment()

        fun showCourseDetailBottomSheet(activity: AppCompatActivity, course: Course) {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager
                    .findFragmentByTag(TAG_SHARE_BS_DIALOG_FRAGMENT) as? MultiCourseDetailFragment
            if (fragment == null) {
                fragment = cacheFragment
            }
            fragment.refreshCourse(course = course)
            fragment.show(fragmentManager, TAG_SHARE_BS_DIALOG_FRAGMENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_frag_multi_course, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tl_multi_sheet_root)
        val viewpager = view.findViewById<ViewPager>(R.id.vp_multi_sheet)
        adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int) = fragments[position]
            override fun getCount() = fragments.size
            override fun getPageTitle(position: Int) = courses[position].coursename
        }
        viewpager.adapter = adapter
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.rootView.viewTreeObserver.addOnGlobalLayoutListener {
            tabLayoutHeight = tabLayout.height
        }
        return view
    }

    fun refreshCourse(course: Course) {
        fragments.removeAll { true }
        courses.removeAll { true }

        fragments.add(CourseDetailFragment(course))
        courses.add(course)

        course.next.forEach {
            val fragment = CourseDetailFragment(it)
            fragments.add(fragment)
            courses.add(it)
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        if (dialog == null) return

//        val view: View = LayoutInflater.from(context).inflate(R.layout.schedule_frag_multi_course,null)
        val view = onCreateView(LayoutInflater.from(context), null, null)!!
        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)
        adapter.notifyDataSetChanged()

        /**
         * 蜜汁生命周期...
         * @see https://stackoverflow.com/questions/37555403/android-bottomsheetdialogfragment-does-not-expand-completely
         */
        getDialog().setOnShowListener { theDialog ->
            val d = theDialog as BottomSheetDialog
            val bottomSheet1 = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val coordinatorLayout = bottomSheet1.parent as CoordinatorLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet1)
            bottomSheetBehavior.peekHeight = fragments[0].totalCollapsedHeight + tabLayoutHeight
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            coordinatorLayout.parent.requestLayout()
        }

    }
}