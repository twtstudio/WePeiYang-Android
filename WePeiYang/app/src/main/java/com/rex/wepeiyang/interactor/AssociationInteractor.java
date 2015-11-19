package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.news.associations.OnGetAssociationCallback;

/**
 * Created by sunjuntao on 15/11/19.
 */
public interface AssociationInteractor {
    void getAssociationList(int page, OnGetAssociationCallback onGetAssociationCallback);
}
