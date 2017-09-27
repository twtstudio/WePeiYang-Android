package com.twt.service.network.view.wifi;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.twt.service.network.command.WiFiLoginViewModel;
import com.twt.service.network.R;
import com.twt.service.network.databinding.FragmentWifiMainLoginBinding;

/**
 * Created by chen on 2017/7/7.
 */

public class WifiFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CheckBox mCheckBox = null;
    private WiFiLoginViewModel loginViewModel;
    private View view;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentWifiMainLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_main_login, container, false);
        loginViewModel = new WiFiLoginViewModel(this);
        binding.setViewModel(loginViewModel);
        view = binding.getRoot();
        mCheckBox = (CheckBox) view.findViewById(R.id.fragment_wifi_checkbox);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_wifi_ll);

        mSharedPreferences = this.getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        initSwipe();

        return view;
    }

    public void initChecked() {
        if (mSharedPreferences.getBoolean("checkboxBoolean", true)) {
            loginViewModel.username.set(mSharedPreferences.getString("username", null));
            loginViewModel.password.set(mSharedPreferences.getString("password", null));
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }

    public void check() {
        mCheckBox.setOnClickListener(v -> {
            if (mCheckBox.isChecked()) {
                Log.d("cccc", "checked");
                editor.putString("username", loginViewModel.username.get());
                editor.putString("password", loginViewModel.password.get());
                editor.putBoolean("checkboxBoolean", true);
                editor.commit();
            }
            if (!mCheckBox.isChecked()) {
                Log.d("cccc", "unchecked");
                editor.putString("username", null);
                editor.putString("password", null);
                editor.putBoolean("checkboxBoolean", false);
                editor.commit();
            }
        });
    }

    private void initSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (loginViewModel.isRefresh) {
                loginViewModel.resetData();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }


}
