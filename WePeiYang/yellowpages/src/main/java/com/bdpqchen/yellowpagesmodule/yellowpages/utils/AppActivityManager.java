package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import android.app.Activity;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Stack;

/**
 * Created by chen on 17-2-21.
 */

public class AppActivityManager {
    private Stack<Activity> mActivityStack;

    private static AppActivityManager mInstance;

    private AppActivityManager(){}

    public static AppActivityManager getActivityManager(){
        if(mInstance == null){
            mInstance = new AppActivityManager();
        }
        return mInstance;
    }

    public void addActivity(Activity activity){
        if(mActivityStack == null){
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    //结束当前的activity
    public void finishCurrentActivity(){
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if(activity != null){
            mActivityStack.remove(activity);
            activity.finish();
        }else{
            Logger.d("You want to remove the activity, but it is null");
        }
    }

    //结束指定名称的activity
    public void finishNamedActivity(Class<?>cls){
        for (Activity activity : mActivityStack){
            if(activity.getClass() == cls){
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity(){
        int i = 0;
        for (Activity activity : mActivityStack){
            if(activity != null){
                activity.finish();
                i++;
            }
        }
        Logger.d("had finished " + i + " activity");
    }

    public void appExit(Context context){
        finishAllActivity();
        System.exit(0);
    }


}
