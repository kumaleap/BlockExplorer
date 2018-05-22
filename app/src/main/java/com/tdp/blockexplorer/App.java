package com.tdp.blockexplorer;

import android.app.Application;

import org.xutils.x;

/**
 * Created by wulijie on 2018/4/11.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(Conf.isDebug);
    }
}
