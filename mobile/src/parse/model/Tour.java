package com.mateoj.hack2help.data.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jose on 10/23/15.
 */
@ParseClassName("Tour")
public class Tour extends ParseObject{
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THUMB = "thumbnail";

    public String getTitle()
    {
        return getString(KEY_TITLE);
    }

    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }

    public String getThumbUrl()
    {
        return getString(KEY_THUMB);
    }

    public void setTitle(String title)
    {
        put(KEY_TITLE, title);
    }

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

}
