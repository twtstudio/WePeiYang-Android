package com.twt.service.ui.lostfound.found.details;

import com.twt.service.bean.FoundDetails;

/**
 * Created by sunjuntao on 16/2/20.
 */
public interface FoundDetailsView {
    void showProgress();

    void hideProgress();

    void toastMessage(String message);

    void bindData(FoundDetails details);
}
