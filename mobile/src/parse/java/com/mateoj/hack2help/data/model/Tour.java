package com.mateoj.hack2help.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mateoj.hack2help.util.Callback;
import com.mateoj.hack2help.util.Error;
import com.mateoj.hack2help.util.StringUtils;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by jose on 10/23/15.
 */
@ParseClassName("Tour")
public class Tour extends ParseObject implements Parcelable{
    public static final String KEY_TITLE = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THUMB = "thumb";
    public static final String KEY_NODE_RELATION = "point";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DURATION = "duration";

    private List<Node> nodes;

    public Tour()
    {

    }

    protected Tour(Parcel in) {
        setTitle(in.readString());
        setDescription(in.readString());
        setThumbUrl(in.readString());
        setDistance(in.readDouble());
        setDuration(in.readDouble());
    }

    public static final Creator<Tour> CREATOR = new Creator<Tour>() {
        @Override
        public Tour createFromParcel(Parcel in) {
            return new Tour(in);
        }

        @Override
        public Tour[] newArray(int size) {
            return new Tour[size];
        }
    };

    public String getTitle()
    {
        return StringUtils.notNull(getString(KEY_TITLE));
    }

    public String getDescription()
    {
        return StringUtils.notNull(getString(KEY_DESCRIPTION));
    }

    public String getThumbUrl()
    {
        return (getParseFile(KEY_THUMB)) == null ? "" : getParseFile(KEY_THUMB).getUrl();
    }

    public double getDistance()
    {
        return getDouble(KEY_DISTANCE);
    }

    public double getDuration()
    {
        return getDouble(KEY_DURATION);
    }

    public void setDistance(double distance)
    {
        put(KEY_DISTANCE, distance);
    }

    public void setDuration(double duration)
    {
        put(KEY_DURATION, duration);
    }

    public void setThumbUrl(String url)
    {
        put(KEY_THUMB, url);
    }

    public void setTitle(String title)
    {
        put(KEY_TITLE, title);
    }

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

    public ParseRelation<Node> getNodeRelation()
    {
        return getRelation(KEY_NODE_RELATION);
    }

    public static ParseQuery<Tour> getTourQuery()
    {
        return new ParseQuery<Tour>("Tour");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getDescription());
        parcel.writeString(getThumbUrl());
        parcel.writeDouble(getDistance());
        parcel.writeDouble(getDuration());
    }

    public void getNodes(final Callback<List<Node>> cb)
    {
        if (nodes == null)
        {
            getNodeRelation()
                    .getQuery()
                    .findInBackground(new FindCallback<Node>() {
                        @Override
                        public void done(List<Node> objects, ParseException e) {
                            if (e != null) {
                                cb.error(new Error(e.getMessage(), e.getCode()));
                                e.printStackTrace();
                            } else {
                                nodes = objects;
                                cb.done(nodes);
                            }
                        }
                    });
        } else {
            cb.done(nodes);
        }
    }
}
