package com.twt.service.ui.bus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class BusActivity extends WebAppActivity implements BusView {


    private static final String URL = "http://bus.twtstudio.com/tp_bus/index.php/Home/Index/adl";
    private static final String TOKEN_REFRESH_HANDLER = "tokenRefreshHandler_Android";
    private static final String TOKEN_ACQUIRE_HANDLER = "tokenAcquireHandler_Android";
    private static final String TOKEN_HANDLER = "tokenHandler_Android";
    @InjectView(R.id.wv_bus)
    WebApp wvBus;
    private BusPresenter presenter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BusActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        ButterKnife.inject(this);
        setWebApp(wvBus);
        presenter = new BusPresenterImpl(this);
        wvBus.loadUrl(URL);
        wvBus.registerHandler(TOKEN_HANDLER, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(PrefUtils.getToken());
            }
        });
        wvBus.registerHandler(TOKEN_REFRESH_HANDLER, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                presenter.tokenRefresh();
            }
        });
        wvBus.registerHandler(TOKEN_ACQUIRE_HANDLER, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                startLoginActivity();
            }
        });

    }

    public void onEvent(TokenRefreshSuccessEvent event) {
        wvBus.loadUrl(URL);
    }

    public void onEvent(TokenRefreshFailureEvent event){
        presenter.onTokenRefreshFailure(event.getError());
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.actionStart(this, NextActivity.Bus);
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
