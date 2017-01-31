package com.twt.service.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.twt.service.support.PrefUtils;

/**
 * Created by sunjuntao on 16/5/4.
 */
public class WebAppActivity extends AppCompatActivity {

    private WebApp mWebApp;
    private static final String NAV_HANDLER = "navigationHandler_Android";


    public void setWebApp(WebApp webApp) {
        this.mWebApp = webApp;
    }

    @Override
    protected void onResume() {
        Log.e("UA", mWebApp.getSettings().getUserAgentString());
        if (mWebApp != null) {
            mWebApp.registerHandler(NAV_HANDLER, new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    finish();
                }
            });
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebApp.destroy();
    }
}
