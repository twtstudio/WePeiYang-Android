package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.news.collegenews.OnGetCollegeNewsCallback;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface CollegeNewsInteractor {
    void getCollegeNewslist(int page, OnGetCollegeNewsCallback onGetCollegeNewsCallback);
}
