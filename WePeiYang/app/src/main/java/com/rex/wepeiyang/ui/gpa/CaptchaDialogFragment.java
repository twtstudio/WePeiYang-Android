package com.rex.wepeiyang.ui.gpa;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rex.wepeiyang.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/25.
 */
public class CaptchaDialogFragment extends DialogFragment implements View.OnClickListener {
    @InjectView(R.id.iv_captcha)
    ImageView ivCaptcha;
    @InjectView(R.id.btn_captcha_cancle)
    TextView btnCaptchaCancle;
    @InjectView(R.id.btn_captcha_submit)
    TextView btnCaptchaSubmit;
    @InjectView(R.id.et_captcha)
    EditText etCpatcha;
    private String token;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_captchadialog, container, false);
        ButterKnife.inject(this, view);
        token = getArguments().getString("token");
        String raw = getArguments().getString("raw");
        if (raw != null) {
            byte[] decodeString = Base64.decode(raw, Base64.DEFAULT);
            Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            ivCaptcha.setImageBitmap(decodeByte);
        }
        btnCaptchaCancle.setOnClickListener(this);
        btnCaptchaSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_captcha_cancle:
                this.dismiss();
                break;
            case R.id.btn_captcha_submit:
                String captcha = etCpatcha.getText().toString();
                if (captcha.isEmpty()) {
                    etCpatcha.setError("不能为空");
                } else {
                    GpaActivity.presenter.getGpaWithToken(token,captcha);
                    this.dismiss();
                }

        }
    }
}
