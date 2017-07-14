package com.twt.service.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.auth.login.AuthSelfProvider;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twt.service.R;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by retrox on 01/03/2017.
 */

public class LibBindFragment extends SlideFragment {

    private Unbinder unbinder;
    private boolean canMoveForward = false;
    private Context mContext;
    @BindView(R.id.lib_password)
    TextInputEditText libPasswordEdit;
    @BindView(R.id.btn_lib_bind)
    Button button;

    @OnClick(R.id.btn_lib_bind)
    public void bind_lib(View view) {

        String libPassWd = libPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(libPassWd)) {
            libPassWd = "000000";
            Toast.makeText(mContext, "尝试默认密码ing...", Toast.LENGTH_SHORT).show();
        }
        new TjuLibProvider(this.getContext()).bindLibrary(integer -> {
            if (integer == -1) {
                canMoveForward = true;
                Toast.makeText(this.getContext(), "图书馆绑定完成,点击底部右侧对勾开始新旅程", Toast.LENGTH_SHORT).show();
            } else if (integer == 50003) {
                canMoveForward = true;
                Toast.makeText(this.getContext(), "图书馆已绑定,点击底部右侧对勾开始新旅程", Toast.LENGTH_SHORT).show();
            } else if (integer == 50002) {
                Toast.makeText(this.getContext(), "图书馆密码错误TAT...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getContext(), "未知错误TAT...", Toast.LENGTH_SHORT).show();
            }
            CommonPrefUtil.setIsBindLibrary(true);
            new AuthSelfProvider().getUserData();
        }, libPassWd);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lib_bind_slide, container, false);
        canMoveForward = CommonPrefUtil.getIsBindLibrary();
        unbinder = ButterKnife.bind(this, view);
        mContext = this.getContext();
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.white_color;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int buttonsColor() {
        return R.color.intro_slide_buttons;
    }

    @Override
    public boolean canMoveFurther() {
        // TODO: 14/03/2017 强制绑定？？？
//        return this.canMoveForward;
        return true;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "请绑定图书馆账号！";
    }
}
