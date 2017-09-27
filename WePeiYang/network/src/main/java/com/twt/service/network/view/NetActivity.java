package com.twt.service.network.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import com.twt.service.network.R;
import com.twt.service.network.R2;
import com.twt.service.network.WifiStatusClass;
import com.twt.service.network.view.set.SetActivity;
import com.twt.service.network.view.widget.WidgetUpdateManager;
import com.twt.service.network.view.wifi.WiFiConnectFragment;
import com.twt.service.network.view.wifi.WifiFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chen on 2017/7/7.
 */
@Route(path = "/network/main")
public class NetActivity extends RxAppCompatActivity {
    @BindView(R2.id.main_net_toolbar)
    Toolbar mToolbar;
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;
    private Unbinder mUnbinder;
    private LayoutInflater inflater;
    //    private int mIv[] = {R.drawable.network_wifi_tab, R.drawable.network_account_tab, R.drawable.network_spy_tab};
//    private String mTv[] = {"wifi认证", "账户", "监控"};
//    private Class fragmentArray[] = {WifiFragment.class, AccountFragment.class, SpyFragment.class};
    public static final String TOKEN1 = "autoConnection";
    //public static final String TOKEN2 = "autoDisconnection";
    private WifiStatusClass wifiStatusClass;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_main1);
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        setTitle("上网");
        mToolbar.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(NetActivity.this, SetActivity.class);
            startActivity(intent);
            return true;
        });
        wifiStatusClass = new WifiStatusClass(this);
        mSharedPreferences = this.getSharedPreferences("wlan", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        initWlan();
        initTab2();
        WidgetUpdateManager.sendUpdateMsg(this);
        String name = wifiStatusClass.getWifiInfo();
        switch (name) {
            case "\"" + "TwT_Studio" + "\"":
                Log.d("wwww", "gatewaytwt:" + wifiStatusClass.getGateway());
                break;
            case "\"" + "tjuwlan" + "\"":
                String gate = wifiStatusClass.getGateway();
                if (gate.equals("172.23.0.1")) {
                    Log.d("wwww", "gateway:successful");
                } else {
                    Log.d("wwww", "gateway:没输出");
                }
                Log.d("wwww", "gatewaytju:" + gate);
                break;
            default:
                Log.d("wwww", "gateway:" + "null");
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_set, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWlan() {
        Log.d("ffff", "initwlan");
        Log.d("ffff", "share:" + mSharedPreferences.getString("autoConnection", null));
        if (mSharedPreferences.getString("autoConnection", null) != null) {
            Log.d("ffff", "!=null");
            switch (mSharedPreferences.getString("autoConnection", null)) {
                case "1":
                    connectTju();
                    break;
                case "2":
                    connectLib();
                    break;
                default:
                    break;
            }
        }
        Messenger.getDefault().register(this, TOKEN1, String.class, String -> {
            mEditor.putString("autoConnection", String);
            mEditor.commit();
            if (String == "3") {
                mEditor.putString("autoConnection", null);
                mEditor.commit();
            }
        });
    }

//    private void initTab() {
//        inflater = LayoutInflater.from(this);
//        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_net_content);
//        int count = fragmentArray.length;
//        for (int i = 0; i < count; i++) {
//            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTv[i]).setIndicator(getTabItemView(i));
//            mTabHost.addTab(tabSpec, fragmentArray[i], null);
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
//        }
//    }

//    private View getTabItemView(int index) {
//        View view = inflater.inflate(R.layout.tab_layout, null);
//
//        ImageView imageView = (ImageView) view.findViewById(R.id.tab_iv);
//        imageView.setImageResource(mIv[index]);
//
//        TextView textView = (TextView) view.findViewById(R.id.tab_tv);
//        textView.setText(mTv[index]);
//
//        return view;
//    }

    private void initTab2() {
        inflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_net_content);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1").setIndicator(getTabView(0));
        mTabHost.addTab(tabSpec, WifiFragment.class, null);
        mTabHost.getTabWidget().setBackground(null);
    }

    //感觉这样很奇怪
    private void initTab3() {
        inflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_net_content);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1").setIndicator(getTabView(0));
        mTabHost.addTab(tabSpec, WiFiConnectFragment.class, null);
        mTabHost.getTabWidget().setBackground(null);
    }

    private View getTabView(int index) {
        View view = inflater.inflate(R.layout.tab_first_edition, null);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void connectTju() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifiStatusClass.openWifi();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name = wifiStatusClass.getWifiInfo();
                String gate = wifiStatusClass.getGateway();
                if (wifiStatusClass.checkStatus() == 3) {
                    switch (name) {
                        case "\"" + "tjuwlan" + "\"":
                            if (gate.equals("172.23.0.1")) {
                                wifiStatusClass.addNetWork(wifiStatusClass.creatWifiInfo("tjuwlan", "", 0));
                            }
                            Log.d("wwww", "gatewaytju:" + wifiStatusClass.getGateway());
                            break;
                        default:
                            Log.d("wwww", "gateway:" + "null");
                            break;
                    }

                }
            }
        }).start();
    }

    private void connectLib() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifiStatusClass = new WifiStatusClass(NetActivity.this);
                wifiStatusClass.openWifi();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name = wifiStatusClass.getWifiInfo();
                //String gate = wifiStatusClass.getGateway();
                if (wifiStatusClass.checkStatus() == 3) {
                    switch (name) {
                        case "\"" + "tjuwlan-lib" + "\"":
                            wifiStatusClass.addNetWork(wifiStatusClass.creatWifiInfo("tjuwlan-lib", "", 0));
                            Log.d("wwww", "gatewaytju:" + wifiStatusClass.getGateway());
                            break;
                        default:
                            Log.d("wwww", "gateway:" + "null");
                            break;
                    }

                }
            }
        }).start();
    }


}
