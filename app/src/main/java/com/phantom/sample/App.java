package com.phantom.sample;

import androidx.multidex.MultiDex;

import com.facebook.stetho.Stetho;

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
        Stetho.initializeWithDefaults(this);
    }

    public static App getContext() {
        return mContext;
    }
}
