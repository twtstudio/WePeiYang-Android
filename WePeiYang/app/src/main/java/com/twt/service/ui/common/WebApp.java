package com.twt.service.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.twt.service.support.ApplicationUtils;

/**
 * Created by sunjuntao on 16/5/3.
 */
public class WebApp extends BridgeWebView {


    public WebApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebApp(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public WebApp(Context context) {
        super(context);
        init();
    }

    private void init() {
        String defaultUA = this.getSettings().getUserAgentString();
        this.getSettings().setUserAgentString(defaultUA + "WePeiYang_Android/" + ApplicationUtils.getVersionName());
    }
}
