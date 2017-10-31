package com.twt.service.network.view.wifi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.orhanobut.hawk.Hawk;
import com.twt.service.network.R;
import com.twt.service.network.command.WifiLoginCommand;

/**
 * Created by chen on 2017/7/7.
 */

public class WifiFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CheckBox mCheckBox = null;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginBt;
    private Button mLogoutBt;
    private Button mSelfService;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wifi_main_login, container, false);

        mCheckBox = (CheckBox) view.findViewById(R.id.fragment_wifi_checkbox);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_wifi_ll);
        mUsername = (EditText) view.findViewById(R.id.fragment_wifi_username);
        mPassword = (EditText) view.findViewById(R.id.fragment_wifi_password);
        mLoginBt = (Button) view.findViewById(R.id.fragment_wifi_login_bt);
        mLogoutBt = (Button) view.findViewById(R.id.fragment_wifi_logout_bt);
        mSelfService = (Button) view.findViewById(R.id.fragment_wifi_self_service_bt);

        Hawk.init(WifiFragment.this.getContext()).build();

        initCheck();
        initSwipe();
        initLog();

        return view;
    }

    public void initCheck() {
        //初始化checkbox
        if (Hawk.get("checkboxBoolean") != null) {
            String check = Hawk.get("checkboxBoolean");
            Log.d("cccc", "checkbox: " + check);
            if (check .equals("1")) {
                mUsername.setText(Hawk.get("username"));
                mPassword.setText(Hawk.get("password"));
                mCheckBox.setChecked(true);
                Log.d("cccc", "checkbox1: " + Hawk.get("checkboxBoolean"));
            } else if (check.equals("2")) {
                mCheckBox.setChecked(false);
            }
        }
        mCheckBox.setOnClickListener(v -> {
            if (mCheckBox.isChecked()) {
                Log.d("cccc", "checked");
                Hawk.put("username", mUsername.getText().toString());
                Hawk.put("password", mPassword.getText().toString());
                Hawk.put("checkboxBoolean", "1");
            }
            if (!mCheckBox.isChecked()) {
                Log.d("cccc", "unchecked");
                Hawk.put("username", null);
                Hawk.put("password", null);
                Hawk.put("checkboxBoolean", "2");
            }
        });
    }

    public void initLog() {
        WifiLoginCommand wifiLoginCommand = new WifiLoginCommand(WifiFragment.this, mUsername, mPassword);
        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cccc", "login: " + "onlogin");
//                WifiLoginCommand wifiLoginCommand = new WifiLoginCommand(WifiFragment.this, mUsername, mPassword);
                wifiLoginCommand.login();
            }
        });
        mLogoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiLoginCommand.logout();
            }
        });
        mSelfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiLoginCommand.onSelfService();
            }
        });
    }

    private void initSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            WifiLoginCommand command = new WifiLoginCommand(WifiFragment.this, mUsername, mPassword);
            command.resetData();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }


}
