package com.twt.service.home.user;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.auth.login.AuthSelfProvider;
import com.twt.service.R;

/**
 * Created by retrox on 2017/1/14.
 */

public class AvatarItemViewModel implements ViewModel {

    private Context mContext;

    //field
    public final ObservableField<String> avatarUrl = new ObservableField<>();
    public final ObservableField<String> userName = new ObservableField<>("天外天账户名");
    public final ObservableField<String> intro = new ObservableField<>("这个人很懒没有写简介");
    public final ObservableInt defultAvatar = new ObservableInt(R.drawable.ic_user);

    public final ReplyCommand onEditClick = new ReplyCommand(this::editIntro);

    private void editIntro() {
        // TODO: 2017/1/14 jump to edit intro activity
    }

    public AvatarItemViewModel() {
        //avatarUrl.set();
        getData();
    }

    public void getData() {
        AuthSelfProvider provider = new AuthSelfProvider();
        provider.getUserData(authSelfBean -> {
            userName.set(authSelfBean.twtuname);
            intro.set(authSelfBean.realname);
            avatarUrl.set(authSelfBean.avatar);
        });
    }
}
