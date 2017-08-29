package com.example.untilsone;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 王爱诗 on 2017/8/29.
 */

public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
