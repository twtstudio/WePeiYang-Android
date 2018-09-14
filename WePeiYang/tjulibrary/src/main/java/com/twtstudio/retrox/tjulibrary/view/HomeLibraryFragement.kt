package com.twtstudio.retrox.tjulibrary.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.provider.Info
import com.twtstudio.retrox.tjulibrary.tjulibservice.Img
import com.twtstudio.retrox.tjulibrary.tjulibservice.LibraryApi
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class HomeLibraryFragement : Fragment(){
    lateinit var button: Button
    lateinit var my_pager : ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.lb_fragment_home,container,false)
        button = view.findViewById(R.id.secondBorrow)
        my_pager = view.findViewById(R.id.view_pager)
        val mylistPagerAdapter = BookPagerAdapter(childFragmentManager)
        launch(UI + QuietCoroutineExceptionHandler) {
            val mylist : CommonBody<Info> = LibraryApi.getUser().await()
            if (mylist.error_code == -1){

                for (i in mylist.data!!.books) {
                    val img: Img = LibraryApi.getImg(/*i.type*/0).await()

                    mylistPagerAdapter.add(BookFragment.newInstance(i, img.img_url, "emmmmm"))

                    button.setOnClickListener {
                        val pop = BookPopupWindow(i, it.context)
                        pop.show()
                    }
                }
            }
        }



        //mylistPagerAdapter.add(BookFragment.newInstance())



        return view
    }
}