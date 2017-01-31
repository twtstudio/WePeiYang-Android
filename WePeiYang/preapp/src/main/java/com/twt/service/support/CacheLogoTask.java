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
public class CacheLogoTask implements Runnable {

    private Context context;

    public CacheLogoTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        File logo = new File(context.getCacheDir(), "logo.jpg");
        InputStream is = context.getResources().openRawResource(R.raw.logo);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(logo);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, read);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
