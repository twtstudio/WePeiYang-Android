package com.twtstudio.retrox.bike.read;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class DebugActivity extends AppCompatActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DebugActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_debug);
//        Button button = (Button) findViewById(R.id.button_debug);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DebugActivity.this,BookDetailActivity.class);
//                startActivity(intent);
//            }
//        });
//        Button fakeToken = (Button) findViewById(R.id.button_debug_token);
//        fakeToken.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PrefUtils.setReadToken("Bearer {eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaXNzIjoiaHR0cDpcL1wvdGFrb29jdG9wdXMuY29tXC95dWVwZWl5YW5nXC9wdWJsaWNcL2FwaVwvYXV0aFwvdG9rZW5cL2dldCIsImlhdCI6MTQ3NzY2NDkxMywiZXhwIjoxNDc4MjY5NzEzLCJuYmYiOjE0Nzc2NjQ5MTMsImp0aSI6ImUxYzkxYWY4ZWE0ZGYyZGYyNmQ0MmI2ZDljNmEyZDE0In0.RBwRqT0ycDDjEdF6vw9FZSwpkFJHTkz6Ghqa34mcLDQ}");
//            }
//        });
    }
}
