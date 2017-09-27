package com.twt.service.network.view.wifi;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.network.R;
import com.twt.service.network.command.WiFiConnectViewModel;
import com.twt.service.network.databinding.FragmentWifiMainConnectBinding;

/**
 * Created by chen on 2017/7/12.
 */

public class WiFiConnectFragment extends Fragment {
    private WiFiConnectViewModel mWiFiConnectViewModel;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public WiFiConnectFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        FragmentWifiMainConnectBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_main_connect, container, false);
        mWiFiConnectViewModel = new WiFiConnectViewModel(this);
        binding.setViewModel(mWiFiConnectViewModel);
        view = binding.getRoot();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_wifi_ll);
        initSwipe();
        return view;
    }

    public void initSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
    }


}
