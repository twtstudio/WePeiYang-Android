package com.twt.service.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.twt.service.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sunjuntao on 16/2/5.
 */
public class CacheLogoTask implements Runnable{

    private Context context;

    public CacheLogoTask(Context context){
        this.context = context;
    }
    @Override
    public void run() {
        File logo = new File(context.getCacheDir(), "logo.png");
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
