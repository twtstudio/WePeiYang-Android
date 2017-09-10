package com.twt.service.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.mukesh.MarkdownView;
import com.twt.service.base.BaseActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by retrox on 01/04/2017.
 */

public class DevTalkActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show();

        MarkdownView markdownView = new MarkdownView(DevTalkActivity.this);
        setContentView(markdownView);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/life2015/WePeiYangDevTalking/master/README.md")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DevTalkActivity.this, "网络出错了TAT...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DevTalkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            markdownView.setMarkDownText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
}
