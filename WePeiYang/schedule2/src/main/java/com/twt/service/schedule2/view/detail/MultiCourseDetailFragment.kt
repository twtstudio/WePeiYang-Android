package com.twt.service.schedule2.view.detail

import android.app.Dialog
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.laenger.android.vpbs.BottomSheetUtils
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior
import biz.laenger.android.vpbs.ViewPagerBottomSheetDialogFragment
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Course

/**
 * 这个是直接用在底部的 因为多门课程冲突的时候 需要用一个TabLayout
 */
class CourseDetailViewHolder(val itemView: View) {
    val recyclerView: RecyclerView = itemView.findViewById(R.id.rcv_sheet)
    val context: Context = recyclerView.context
    var totalCollapsedHeight = 0
    val layoutManager = LinearLayoutManager(context)
    val adapter = CourseDetailAdapter(context)

    init {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun bind(course: Course) {
        adapter.refreshDataList(createCourseDetailList(course))

        itemView.rootView.viewTreeObserver.addOnGlobalLayoutListener {
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
    }

    companion object {
        fun create(inflater: LayoutInflater, container: ViewGroup?): View {
            return inflater.inflate(R.layout.schedule_frag_bottom_sheet, container, false)
        }
    }
}


class MultiCourseDetailFragment : ViewPagerBottomSheetDialogFragment() {
    private lateinit var adapter: MultiCoursePagerAdapter
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


    fun refreshCourse(course: Course) {
        courses.removeAll { true }
        courses.add(course)

        course.next.forEach {
            courses.add(it)
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        if (dialog == null) return

//        val view: View = LayoutInflater.from(context).inflate(R.layout.schedule_frag_multi_course,null)
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_frag_multi_course, null, false)
        dialog.setContentView(view)
        val tabLayout = view.findViewById<TabLayout>(R.id.tl_multi_sheet_root)
        val viewpager = view.findViewById<ViewPager>(R.id.vp_multi_sheet)
        adapter = MultiCoursePagerAdapter()
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 4
        tabLayout.setupWithViewPager(viewpager)
        BottomSheetUtils.setupViewPager(viewpager)
        adapter.notifyDataSetChanged()
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        val bottomSheetBehavior = ViewPagerBottomSheetBehavior.from(bottomSheet)

        tabLayout.rootView.viewTreeObserver.addOnGlobalLayoutListener {
            tabLayoutHeight = tabLayout.height
        }

        dialog.setOnShowListener {
            val detailLayoutHeight = adapter.firstHolder?.totalCollapsedHeight ?:100
            bottomSheetBehavior.peekHeight = detailLayoutHeight + tabLayoutHeight
            bottomSheetBehavior.state = ViewPagerBottomSheetBehavior.STATE_COLLAPSED
        }

    }

    inner class MultiCoursePagerAdapter : PagerAdapter() {
        var firstHolder: CourseDetailViewHolder? = null

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return `object` == view
        }

        override fun getCount(): Int = courses.size

        override fun getPageTitle(position: Int) = courses[position].coursename

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = CourseDetailViewHolder.create(LayoutInflater.from(context), container)
            container.addView(view)
            val courseDetailViewHolder = CourseDetailViewHolder(view)
            courseDetailViewHolder.bind(courses[position])
            if (position == 0) {
                firstHolder = courseDetailViewHolder
            }
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}

