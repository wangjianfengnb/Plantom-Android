package com.phantom.sample;

import androidx.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.phantom.client.ImClient;

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
        ImClient.getInstance().initialize(this, BuildConfig.SERVER_URL);
    }

    public static App getContext() {
        return mContext;
    }
}
