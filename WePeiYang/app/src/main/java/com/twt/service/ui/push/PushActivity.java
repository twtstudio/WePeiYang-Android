package com.twt.service.ui.push;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twt.service.R;
import com.twt.service.service.push.PushService;
import com.twt.service.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PushActivity extends BaseActivity {

    @InjectView(R.id.stop_push_btn)
    Button stopPush;
    @InjectView(R.id.start_push_btn)
    Button startPush;

    public static void actionStart(Context context)
    {
        Intent intent=new Intent(context,PushActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ButterKnife.inject(this);
        stopPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PushActivity.this, PushService.class);
                stopService(intent);
            }
        });
        startPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PushActivity.this,PushService.class);
                startService(intent);
            }
        });
    }
}
