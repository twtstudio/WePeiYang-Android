package com.twt.service.network;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by chen on 2017/7/15.
 */
//用户所在地有其他已经配置好的&有密码的网络 会崩
public class WifiStatusClass {
    private Activity mActivity;
    private Context mContext;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private ScanResult mScanResult;
    private DhcpInfo mDhcpInfo;
    private static final int WIFICIPHER_NOPASS = 0;
    private static final int WIFICIPHER_WEP = 1;
    private static final int WIFICIPHER_WPA = 2;

    public WifiStatusClass(Activity activity) {
        this.mActivity = activity;
        mWifiManager = (WifiManager) activity.getSystemService(activity.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        mWifiList = mWifiManager.getScanResults();
        mDhcpInfo = mWifiManager.getDhcpInfo();
    }

    public WifiStatusClass(Context context) {
        this.mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        //mWifiList = mWifiManager.getScanResults();
    }

    //获取当前连接到的wifi名称
    public String getWifiInfo() {
        return mWifiInfo.getSSID();
    }

    //int WIFI_STATE_DISABLING = 0; 正在关闭
    //int WIFI_STATE_DISABLED = 1; 不可用
    //int WIFI_STATE_ENABLING = 2; 正在打开
    //int WIFI_STATE_ENABLED = 3; 可用
    //int WIFI_STATE_UNKNOWN = 4; 状态不可知
    //获取wlan打开状态
    public int checkStatus() {
        return mWifiManager.getWifiState();
    }

    //关闭wlan开关
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    //打开wlan开关
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    //连接到指定SSID的WIFI
    public void addNetWork(WifiConfiguration wcg) {
        //得到对应的networkID
        int wcgID = mWifiManager.addNetwork(wcg);
        //enableNetwork用于连接到networkID对应的wifi
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        Log.d("wcgId", "wcgID:" + wcgID);
        Log.d("bbbb", "b:" + b);
    }

    public WifiConfiguration creatWifiInfo(String SSID, String passWord, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        //如果之前有类似的配置
        WifiConfiguration tempConfig = isExists(SSID);
        //清除旧有配置
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        //试一下 清除所有旧有配置
        //removeAll();
        //nopassword
        if (type == WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    //查看以前是否也配置过这个网络
    private WifiConfiguration isExists(String SSID) {
        List<WifiConfiguration> configurationLists = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration configurationList : configurationLists) {
            if (configurationList.SSID.equals("\"" + SSID + "\"")) {
                return configurationList;
            }
        }
        return null;
    }

    //应该写一个方法去除所有的
    //这样会导致用户之前所有的配置失效 不太好
    private void removeAll() {
        List<WifiConfiguration> configurationLists = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuration : configurationLists) {
            if (configuration != null) {
                mWifiManager.removeNetwork(configuration.networkId);
            }
        }
    }


    //用www.baidu.com测试internet
    //不行 太烦了 我先直接写成 登录注销 吧
    //第一版这样会不会太糙了……
    public boolean getInternetStatus() {
        return false;
    }

    public String getGateway() {
        long gateway = mDhcpInfo.gateway;
        String gateway1 = longToGateway(gateway);
        if (gateway1 != null) {
            return gateway1;
        }
        return null;
    }

    private String longToGateway(long gateway) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.valueOf((int) gateway & 0xff));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((int) (gateway >> 8) & 0xff));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((int) (gateway >> 16) & 0xff));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((int) (gateway >> 24) & 0xff));
        return stringBuffer.toString();
    }


}
