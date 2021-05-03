package com.college.miniProject1;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DailyNoteApplication extends Application {
    final ExecutorService exService = Executors.newFixedThreadPool(1);
    final Handler mainUIUpdater = HandlerCompat.createAsync (Looper.getMainLooper());


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
