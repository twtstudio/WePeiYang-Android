package com.twt.service.home.user;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twt.service.R;
import com.twtstudio.retrox.auth.api.AuthProvider;

/**
 * Created by retrox on 2017/1/14.
 */

public class AvatarItemViewModel implements ViewModel {

    //field
    public final ObservableField<String> avatarUrl = new ObservableField<>();
    public final ObservableField<String> userName = new ObservableField<>("天外天账户名");
    public final ObservableField<String> intro = new ObservableField<>("这个人很懒没有写简介");
    public final ObservableInt defultAvatar = new ObservableInt(R.drawable.ic_user);
    public final ReplyCommand onEditClick = new ReplyCommand(this::editIntro);
    private Context mContext;

    public AvatarItemViewModel() {
        //avatarUrl.set();
        getData();
    }

    private void editIntro() {
        // TODO: 2017/1/14 jump to edit intro activity
    }

    public void getData() {
        AuthProvider provider = new AuthProvider();
        provider.getUserData(authSelfBean -> {
            userName.set(authSelfBean.getTwtuname());
            intro.set(authSelfBean.getRealname());
            avatarUrl.set(authSelfBean.getAvatar());
        });
    }
}
