package com.mateoj.hack2help.data.model;

import com.mateoj.hack2help.util.StringUtils;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jose on 10/23/15.
 */
@ParseClassName("Node")
public class Node extends ParseObject {
    public static final String KEY_TITLE = "Ttile";
    public static final String KEY_THUMB = "photo";

    public String getTitle()
    {
        return StringUtils.notNull(getString(KEY_TITLE));
    }

    public String getThumbUrl()
    {
        return (getParseFile(KEY_THUMB)) == null ? "" : getParseFile(KEY_THUMB).getUrl();
    }
}
