package com.twt.service.ui.lostfound.post;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twt.service.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rex on 2015/8/6.
 */
public class PreEditFragment extends Fragment {
    @InjectView(R.id.iv_lost_found_edit)
    ImageView ivLostFoundEdit;
    @InjectView(R.id.tv_lost_found_before_edit_name)
    TextView tvLostFoundBeforeEditName;
    @InjectView(R.id.tv_lost_found_before_edit_number)
    TextView tvLostFoundBeforeEditNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_found_before_edit, container, false);
        ButterKnife.inject(this, view);
        //tvLostFoundBeforeEditName.setText(PreferencesUtils.getString("lost_found_name", "编辑姓名"));
        //tvLostFoundBeforeEditNumber.setText(PreferencesUtils.getString("lost_found_number", "编辑联系方式"));
        ivLostFoundEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.hide(PreEditFragment.this);
                if (PostLostFoundActivity.editFragment.isAdded()) {
                    transaction.show(PostLostFoundActivity.editFragment);
                } else {
                    transaction.add(R.id.fl_post_lost_found, PostLostFoundActivity.editFragment);
                }
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
