package com.twt.service.ui.date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.twt.service.R;
import com.twt.service.support.PrefUtils;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.common.TokenRefreshFailureEvent;
import com.twt.service.ui.common.TokenRefreshSuccessEvent;
import com.twt.service.ui.common.WebApp;
import com.twt.service.ui.common.WebAppActivity;
import com.twt.service.ui.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DatingActivity extends WebAppActivity implements DatingView {

    private static final String URL = "http://yueba.twtstudio.com";
    private static final String TOKEN_REFRESH_HANDLER = "tokenRefreshHandler_Android";
    private static final String TOKEN_ACQUIRE_HANDLER = "tokenAcquireHandler_Android";
    private static final String TOKEN_HANDLER = "tokenHandler_Android";
    private DatingPresenter presenter;
    @InjectView(R.id.wv_dating)
    WebApp wvDating;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DatingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating);
        ButterKnife.inject(this);
        presenter = new DatingPresenterImpl(this);
        wvDating.loadUrl(URL);
        setWebApp(wvDating);
        wvDating.getSettings().setDomStorageEnabled(true);
        /*
         * 刷新token的handler
         */

        wvDating.registerHandler(TOKEN_HANDLER, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(PrefUtils.getToken());
            }
        });
        wvDating.registerHandler(TOKEN_REFRESH_HANDLER, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                presenter.tokenRefresh();
            }
        });
        /*
         * 重新获取token的handler
         */
        wvDating.registerHandler(TOKEN_ACQUIRE_HANDLER, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                startLoginActivity();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvDating.canGoBack()) {
            wvDating.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEvent(TokenRefreshSuccessEvent event) {
        wvDating.loadUrl(URL);
    }

    public void onEvent(TokenRefreshFailureEvent event) {
        presenter.onTokenRefreshFailure(event.getError());
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.actionStart(this, NextActivity.Dating);
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
