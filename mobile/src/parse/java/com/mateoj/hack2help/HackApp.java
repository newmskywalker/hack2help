package com.mateoj.hack2help;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by jose on 10/23/15.
 */
public class HackApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initParse();
    }

    private void initParse()
    {
        Parse.initialize(this,
                getString(R.string.parseAppId),
                getString(R.string.parseAppKey));
        ParseObject.registerSubclass();
    }
}
