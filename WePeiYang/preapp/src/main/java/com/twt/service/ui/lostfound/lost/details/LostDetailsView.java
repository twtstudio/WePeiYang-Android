package com.twt.service.ui.lostfound.lost.details;

import com.twt.service.bean.LostDetails;

/**
 * Created by sunjuntao on 16/2/20.
 */
public interface LostDetailsView {
    void showProgress();

    void hideProgress();

    void bindData(LostDetails details);

    void toastMessage(String message);
}
