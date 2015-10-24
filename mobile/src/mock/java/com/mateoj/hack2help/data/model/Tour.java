package com.mateoj.hack2help.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jose on 10/23/15.
 */
public class Tour implements Parcelable {
    private String title;
    private String description;
    private String thumbUrl;
    private Double distance;
    private Double duration;

    public Tour()
    {

    }

    protected Tour(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbUrl = in.readString();
        duration = in.readDouble();
        distance = in.readDouble();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(thumbUrl);
        parcel.writeDouble(duration);
        parcel.writeDouble(distance);
    }
}
