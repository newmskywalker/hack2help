package com.mateoj.hack2help;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jose on 10/23/15.
 */
public class HackApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this,
                getString(R.string.parseAppId),
                getString(R.string.parseAppKey));
    }
}
