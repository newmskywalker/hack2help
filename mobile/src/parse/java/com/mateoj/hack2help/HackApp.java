package com.mateoj.hack2help;

import android.app.Application;

import com.mateoj.hack2help.data.model.Node;
import com.mateoj.hack2help.data.model.Tour;
import com.parse.Parse;
import com.parse.ParseObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
        ParseObject.registerSubclass(Tour.class);
        ParseObject.registerSubclass(Node.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
