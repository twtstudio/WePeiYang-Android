package com.twt.service.ui.lostfound.post;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.twt.service.R;
import com.twt.service.support.ResourceHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/6.
 */
public class EditFragment extends Fragment {
    @InjectView(R.id.et_lost_found_name)
    EditText etLostFoundName;
    @InjectView(R.id.et_lost_found_number)
    EditText etLostFoundNumber;
    @InjectView(R.id.iv_lost_found_submit)
    ImageView ivLostFoundSubmit;
    private String name;
    private String number;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_found_edit, container, false);
        //etLostFoundName.setText(PreferencesUtils.getString("lost_found_name", ""));
        //etLostFoundNumber.setText(PreferencesUtils.getString("lost_found_number", ""));
        ivLostFoundSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void submit() {
        if (TextUtils.isEmpty(etLostFoundName.getText().toString())) {
            etLostFoundName.setError(ResourceHelper.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(etLostFoundNumber.getText().toString())) {
            etLostFoundNumber.setError(ResourceHelper.getString(R.string.empty_error));
            return;
        }
        //PreferencesUtils.putString("lost_found_name", etLostFoundName.getText().toString());
        //PreferencesUtils.getString("lost_found_number", etLostFoundName.getText().toString());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.hide(EditFragment.this);
        if (PostLostFoundActivity.preEditFragment.isAdded()) {
            transaction.show(PostLostFoundActivity.preEditFragment);
        } else {
            transaction.add(R.id.fl_post_lost_found, PostLostFoundActivity.preEditFragment);
        }
        transaction.commit();
    }
}
