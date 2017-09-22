package com.example.lfapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallActivity;

public class LFapp_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lfapp__main);
        startActivity(new Intent(this, WaterfallActivity.class));
    }
}
