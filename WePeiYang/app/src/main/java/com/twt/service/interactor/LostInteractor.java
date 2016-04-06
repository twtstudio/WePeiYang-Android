package com.twt.service.interactor;

/**
 * Created by Rex on 2015/8/2.
 */
public interface LostInteractor {
    void getLostList(int page);

    void getLostDetails(int id);

    void postLost(String authorization, String title, String name, String time, String place, String phone, String content, String lost_type, String otherTag);

    void getMyLostList(String authorization, int page);

}
