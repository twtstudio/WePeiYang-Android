package com.twtstudio.retrox.wepeiyangrd.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twtstudio.retrox.wepeiyangrd.R;

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
    @BindView(R.id.lib_password)
    EditText libPasswordEdit;

    @OnClick(R.id.btn_lib_bind)
    private void bind_lib(View view) {
        new TjuLibProvider(this.getContext()).bindLibrary(integer -> {
            if (integer == -1) {
                canMoveForward = true;
                Toast.makeText(this.getContext(), "图书馆绑定完成", Toast.LENGTH_SHORT).show();
            } else if (integer == 50003) {
                canMoveForward = true;
                Toast.makeText(this.getContext(), "图书馆已绑定", Toast.LENGTH_SHORT).show();
            } else if (integer == 50002) {
                Toast.makeText(this.getContext(), "图书馆密码错误", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getContext(), "未知错误", Toast.LENGTH_SHORT).show();
            }
        }, libPasswordEdit.getText().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lib_bind_slide, container, false);
        canMoveForward = CommonPrefUtil.getIsBindLibrary();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.lib_slide_background_color;
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
        return this.canMoveForward;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "请绑定图书馆账号！";
    }
}
