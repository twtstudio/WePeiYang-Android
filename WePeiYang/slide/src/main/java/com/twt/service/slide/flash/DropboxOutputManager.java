package com.twt.service.slide.flash;

import android.content.Context;

import android.os.DropBoxManager;

import android.os.Environment;

import android.util.Log;

import com.twt.wepeiyang.commons.experimental.CommonContext;

import java.io.IOException;

public class DropboxOutputManager {

    private static final String TAG = "DropboxPrintManager";

    private static final int MAX_BYTES = 8192 * 100;

// DropBoxManager 是 Android 在 Froyo(API level 8) 引入的用来持续化存储系统数据的机制,

// 主要用于记录 Android 运行过程中, 内核, 系统进程, 用户进程等出现严重问题时的 log,

// 可以认为这是一个可持续存储的系统级别的 logcat.

    private DropBoxManager dropBoxManager;

    /**
     * [dropbox文件生成路径]
     */

    private String outputPath = Environment.getExternalStorageDirectory().

            getPath() + "/log/dropbox.log";

    private static DropboxOutputManager sInstance;

    private DropboxOutputManager() {

        //通过用参数 DROPBOX_SERVICE 调用 getSystemService(String) 来获得这个服务

        dropBoxManager = (DropBoxManager) CommonContext.INSTANCE.getApplication().getApplicationContext().getSystemService(Context.DROPBOX_SERVICE);

    }

    public static DropboxOutputManager getInstance() {

        if (sInstance == null) {

            synchronized (DropboxOutputManager.class) {

                if (sInstance == null) {

                    sInstance = new DropboxOutputManager();

                }

            }

        }

        return sInstance;

    }

    /**
     * 打印日志信息
     */

    public boolean printDropboxLog() throws IOException {

        // 我们要输出所有的dropbox信息，所以时间点从0开始

        long time = 0;

        String text = "";

        DropBoxManager.Entry entry;

        while ((entry = getEntry(time)) != null) {

            //这里给它加个标签

            text = entry.getTag() + "  " + entry.getText(MAX_BYTES) + "\r\n";

            time = entry.getTimeMillis();

            //直接打印出来，当然你可以在这里把text写到文件中去
            if (entry.getTag().equals( "data_app_anr")) {
                Log.d("DropBoxLog", text);
            }

            // 这里一定要记得关闭

            if (entry != null) {

                entry.close();

            }

        }

        return true;

    }

    /**
     * 获取指定时间点后的第一个entry，不指定tag
     */
    private DropBoxManager.Entry getEntry(long time) {

        // 需要在AndroidManifest中增加android.permission.READ_LOGS权限

        DropBoxManager.Entry entry = dropBoxManager.getNextEntry(null, time);

        return entry;

    }

}


