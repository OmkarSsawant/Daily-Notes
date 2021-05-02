package com.college.miniProject1;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DailyNoteApplication extends Application {
    final ExecutorService exService = Executors.newFixedThreadPool(2);
    final Handler mainUIUpdater = HandlerCompat.createAsync (Looper.getMainLooper());
}
