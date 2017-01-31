package com.twt.service.support;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.twt.service.WePeiYangApp;
import com.twt.service.bean.ClassTable;
import com.twt.service.bean.Gpa;
import com.twt.service.bean.Main;
import com.twt.service.ui.gpa.GpaPresenter;
import com.twt.service.ui.gpa.GpaView;
import com.twt.service.ui.main.MainPresenter;
import com.twt.service.ui.main.MainView;
import com.twt.service.ui.schedule.SchedulePresenter;
import com.twt.service.ui.schedule.ScheduleView;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sunjuntao on 16/3/11.
 */
public class FileCacheLoader {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;
    private static final long KEEP_ALIVE = 10L;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "FileCacheLoader#" + mCount.getAndIncrement());
        }
    };
    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), sThreadFactory);
    private static final String GPA = "gpa";
    private static final String SCHEDULE = "classtable";
    private static final String MAIN = "main";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;

    private FileCacheLoader() {
        mContext = WePeiYangApp.getContext();
    }

    public static FileCacheLoader build() {
        return new FileCacheLoader();
    }

    public void getGpa(final GpaPresenter presenter, final GpaView view) {
        final ACache aCache = ACache.get(mContext);
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final String gpaString = aCache.getAsString(GPA);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (gpaString == null) {
                            presenter.getGpaFromNet();
                        } else {
                            Gpa gpa = new Gson().fromJson(gpaString, Gpa.class);
                            view.bindData(gpa);
                            view.setClickable(true);
                        }
                    }
                });
            }
        });
    }

    public void setGpa(final String gpaString) {
        final ACache aCache = ACache.get(mContext);
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                aCache.put(GPA, gpaString);
            }
        });
    }

    public void getSchedule(final SchedulePresenter presenter, final ScheduleView view) {
        final ACache aCache = ACache.get(mContext);
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final String scheduleString = aCache.getAsString(SCHEDULE);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (scheduleString == null) {
                            presenter.loadCoursesFromNet();
                        } else {
                            ClassTable classTable = new Gson().fromJson(scheduleString, ClassTable.class);
                            view.bindData(classTable);
                        }
                    }
                });
            }
        });
    }

    public void setSchedule(final String scheduleString) {
        final ACache aCache = ACache.get(mContext);
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                aCache.put(SCHEDULE, scheduleString);
            }
        });
    }

    public void getMain(final MainPresenter presenter, final MainView view) {
        final ACache aCache = ACache.get(mContext);
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final String mainString = aCache.getAsString(MAIN);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mainString == null) {
                            presenter.loadDataFromNet();
                        } else {
                            Main main = new Gson().fromJson(mainString, Main.class);
                            view.bindData(main);
                        }
                    }
                });
            }
        });
    }

    public void setMain(final String mainString) {
        final ACache aCache = ACache.get(mContext);
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                aCache.put(MAIN, mainString);
            }
        });
    }
}
