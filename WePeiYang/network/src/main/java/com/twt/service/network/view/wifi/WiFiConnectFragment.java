package com.twt.service.network.view.wifi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.twt.service.network.R;
import com.twt.service.network.command.WifiLogoutCommand;

/**
 * Created by chen on 2017/7/12.
 */

public class WiFiConnectFragment extends Fragment {
    private WifiLogoutCommand mWifiLogoutCommand;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private TextView mWifiName;
    private TextView mStudentid;
    private TextView mIPAddress;
    private Button mLogoutBt;
    private Button mSelfService;


    public WiFiConnectFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_main_connect, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_wifi_ll);
        mLogoutBt = (Button) view.findViewById(R.id.fragment_wifi_bt_unconnected);
        mSelfService = (Button) view.findViewById(R.id.fragment_wifi_self_service_bt);
        mWifiName = (TextView) view.findViewById(R.id.item_wifi_net2);
        mStudentid = (TextView) view.findViewById(R.id.item_wifi_studentid2);
        mIPAddress = (TextView) view.findViewById(R.id.item_wifi_ip);
        mWifiLogoutCommand = new WifiLogoutCommand(WiFiConnectFragment.this, mWifiName, mStudentid, mIPAddress);

        initSwipe();
        initText();
        onLog();
        return view;
    }

    public void initText() {
        mWifiLogoutCommand.setText();
    }

    public void onLog() {
        mLogoutBt.setOnClickListener(view -> mWifiLogoutCommand.onLogout());
        mSelfService.setOnClickListener(view -> mWifiLogoutCommand.onSelfService());
    }

    public void initSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
    }


}
