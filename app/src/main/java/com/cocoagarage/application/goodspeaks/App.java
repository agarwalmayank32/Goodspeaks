package com.cocoagarage.application.goodspeaks;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
       // DB will be opened and default data will be loaded.
      //  DBManager.sharedManager(this).getDbHelper().getWritableDatabase();
     //   DBManager.sharedManager(this);
    }
}
