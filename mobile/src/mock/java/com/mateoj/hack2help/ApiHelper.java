package com.mateoj.hack2help;

import com.google.android.gms.maps.model.LatLng;
import com.mateoj.hack2help.data.model.Tour;
import com.mateoj.hack2help.util.Callback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jose on 10/23/15.
 */
public class ApiHelper {
    public static void getToursNearby(LatLng latLng, Callback<List<Tour>> cb) {
        List<Tour> tourList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Tour tour = new Tour();
            tour.setTitle("Super Tour " + i);
            tour.setDescription("The Tour I like is right here");
            tour.setThumbUrl("http://s3-media2.fl.yelpcdn.com/bphoto/3_SyRBrDT2w7WIioMQ0-2Q/o.jpg");
            tour.setDuration(13.5 * (i+1));
            tour.setDistance((double) (2 * (i + 1)));
            tourList.add(tour);
        }
        cb.done(tourList);

    }
}
