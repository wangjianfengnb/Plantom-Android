package com.phantom.sample;

import androidx.multidex.MultiDex;

/**
 * 启动Application
 */
public class App extends android.app.Application {

    private static App mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mContext = this;
    }

    public static App getContext() {
        return mContext;
    }
}
