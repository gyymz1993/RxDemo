package com.rxdemo.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/12/28.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("RxJava").logLevel(LogLevel.FULL).logTool(new AndroidLogTool());
    }
}
