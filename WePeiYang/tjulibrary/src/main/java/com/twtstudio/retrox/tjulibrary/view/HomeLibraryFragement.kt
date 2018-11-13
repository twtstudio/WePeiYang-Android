package com.twtstudio.retrox.tjulibrary.view

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.home.LibraryViewModel
import com.twtstudio.retrox.tjulibrary.tjulibservice.LibraryApi
import com.twtstudio.retrox.tjulibrary.tjulibservice.imgSrc
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.backgroundColor


class HomeLibraryFragement : Fragment(), ViewPager.OnPageChangeListener {


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        LibraryViewModel.infoLiveData.bindNonNull(this) { info ->

            button.setOnClickListener {
                val pop = BookPopupWindow(info.books[position], it.context)
                pop.show()
            }
        }
        linear.getChildAt(mNum).isEnabled = false
        linear.getChildAt(position).isEnabled = true
        mNum = position
    }


    lateinit var button: Button
    lateinit var my_pager: ViewPager
    lateinit var linear: LinearLayout
    lateinit var noBorrow: CardView
    lateinit var progressBar: ProgressBar
    var mNum: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.lib_fragment_home, container, false)
        button = view.findViewById(R.id.secondBorrow)
        my_pager = view.findViewById(R.id.view_pager)
        linear = view.findViewById(R.id.main_linear)
        noBorrow = view.findViewById(R.id.no_borrow)
        progressBar = view.findViewById(R.id.home_progressbar)
        val mylistPagerAdapter = BookPagerAdapter(childFragmentManager)
        LibraryViewModel.infoLiveData.bindNonNull(this) { info ->
            if (mylistPagerAdapter.count == 0) {
                if (info.books.size == 0) {
                    noBorrow.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    button.backgroundColor = Color.parseColor("#cccccc")
                    button.setOnClickListener {
                        Toast.makeText(activity, "你还没有借书yo～，想续借是不是贪心了一点呢_(:3 > <)_", Toast.LENGTH_LONG).show()
                    }
                } else {
                    noBorrow.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    mylistPagerAdapter.removeAll()
                    linear.removeAllViews()
                    launch(UI + QuietCoroutineExceptionHandler) {
                        mylistPagerAdapter.removeAll()
                        linear.removeAllViews()
                        for (i in info.books) {
                            if(i.id == null ) continue
                            val isbnNumber = LibraryApi.getISBN(i.id).await()
                            val url: List<imgSrc>? = LibraryApi.getImgUrl(isbnNumber.isbn).await()
                            if (url!![0].result.isEmpty()){mylistPagerAdapter.add(BookFragment.newInstance(i, "error", i.callno))}else{
                                mylistPagerAdapter.add(BookFragment.newInstance(i, url!![0].result[0].coverlink, i.callno))
                            }
                            val view1 = View(view.context)
                            view1.setBackgroundResource(R.drawable.background)
                            view1.isEnabled = false
                            //设置宽高
                            val layoutParams = LinearLayout.LayoutParams(30, 30)
                            //设置间隔
                            if (i != info.books[0]) {
                                layoutParams.leftMargin = 9
                            }
                            linear.addView(view1, layoutParams)
                        }
                        linear.getChildAt(0).isEnabled = true
                        my_pager.adapter = mylistPagerAdapter
                        progressBar.visibility = View.GONE
                    }
                    button.setOnClickListener {
                        val pop = BookPopupWindow(info.books[0], it.context)
                        pop.show()
                    }
                }
            }
            my_pager.addOnPageChangeListener(this)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        LibraryViewModel.infoLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
    }
}