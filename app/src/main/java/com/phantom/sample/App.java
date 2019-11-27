package com.phantom.sample;

import androidx.multidex.MultiDex;

/**
 * 启动Application
 */
public class App extends android.app.Application {

    private App mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        this.mContext = this;
    }

    public App getContext() {
        return mContext;
    }
}
