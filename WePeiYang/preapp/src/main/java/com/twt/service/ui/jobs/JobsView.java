package com.twt.service.ui.jobs;

import com.twt.service.bean.JobsList;

/**
 * Created by sunjuntao on 16/2/13.
 */
public interface JobsView {

    void hideRefreshing();

    void toastMessage(String message);

    void refresh(JobsList jobsList);

    void loadMore(JobsList jobsList);
}
