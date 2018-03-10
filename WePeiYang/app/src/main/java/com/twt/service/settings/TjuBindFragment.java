package com.twt.service.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twt.wepeiyang.commons.utils.CommonPreferences;
import com.twtstudio.retrox.auth.api.AuthProvider;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 01/03/2017.
 */

public class TjuBindFragment extends SlideFragment {

    @BindView(R.id.tju_num)
    EditText numEdit;
    @BindView(R.id.tju_password)
    EditText passwordEdit;
    private Unbinder unbinder;
    private boolean canMoveFuther = false;

    @OnClick(R.id.btn_tju_bind)
    public void bind(View view) {
        RealAuthApi.INSTANCE
                .bindTju(numEdit.getText().toString(), passwordEdit.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> AuthProvider.INSTANCE.authSelf(false, true))
                .subscribe(responseBody -> {
                    canMoveFuther = true;
                    Toast.makeText(this.getContext(), "绑定成功", Toast.LENGTH_SHORT).show();
                }, new RxErrorHandler());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tju_bind_slide, container, false);
        canMoveFuther = CommonPreferences.INSTANCE.isBindTju();
        unbinder = ButterKnife.bind(this, view);

        return view;
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
        return this.canMoveFuther;
    }

    @Override
    public int backgroundColor() {
        return R.color.white_color;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "办公网尚未绑定";
    }
}
