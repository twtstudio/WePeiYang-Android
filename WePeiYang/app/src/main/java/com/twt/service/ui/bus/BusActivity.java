package com.twt.service.ui.bus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.twt.service.R;
import com.twt.service.support.PrefUtils;
import com.twt.service.support.share.UpLoadWebChromeClient;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.common.TokenRefreshFailureEvent;
import com.twt.service.ui.common.TokenRefreshSuccessEvent;
import com.twt.service.ui.common.WebApp;
import com.twt.service.ui.common.WebAppActivity;
import com.twt.service.ui.login.LoginActivity;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BusActivity extends WebAppActivity implements BusView {


    private static final String URL = "http://bus.twtstudio.com/tp_bus/index.php/Home/Index/adl";
    private static final String TOKEN_REFRESH_HANDLER = "tokenRefreshHandler_Android";
    private static final String TOKEN_ACQUIRE_HANDLER = "tokenAcquireHandler_Android";
    private static final String TOKEN_HANDLER = "tokenHandler_Android";
    private UpLoadWebChromeClient upLoadWebChromeClient;
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
        upLoadWebChromeClient = new UpLoadWebChromeClient(this);
        setWebApp(wvBus);
        presenter = new BusPresenterImpl(this);
        wvBus.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wvBus.getSettings().setDomStorageEnabled(true);
        wvBus.getSettings().setAllowFileAccess(true);
        wvBus.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvBus.setVerticalScrollBarEnabled(true);
        wvBus.getSettings().setJavaScriptEnabled(true);
        wvBus.setWebChromeClient(upLoadWebChromeClient);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==UpLoadWebChromeClient.FILECHOOSER_RESULTCODE)
        {
            if (null == upLoadWebChromeClient.mUploadMessage && upLoadWebChromeClient.mUploadCallbackAboveL == null) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (data == null && upLoadWebChromeClient.mCameraFilePath != ""){
                result = UpLoadWebChromeClient.getImageContentUri(this, new File(upLoadWebChromeClient.mCameraFilePath));
            }
            if (upLoadWebChromeClient.mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }
            else  if (upLoadWebChromeClient.mUploadMessage != null) {
                upLoadWebChromeClient.mUploadMessage.onReceiveValue(result);
                upLoadWebChromeClient.mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != upLoadWebChromeClient.FILECHOOSER_RESULTCODE
                || upLoadWebChromeClient.mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results == null && upLoadWebChromeClient.mCameraFilePath != ""){
            Uri result = UpLoadWebChromeClient.getImageContentUri(this, new File(upLoadWebChromeClient.mCameraFilePath));
            results  = new Uri[]{result};
        }
        upLoadWebChromeClient.mUploadCallbackAboveL.onReceiveValue(results);
        upLoadWebChromeClient.mUploadCallbackAboveL = null;
        return;
    }
}
