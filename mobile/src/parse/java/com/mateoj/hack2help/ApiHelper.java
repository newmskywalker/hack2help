package com.mateoj.hack2help;

import com.google.android.gms.maps.model.LatLng;
import com.mateoj.hack2help.data.model.Tour;
import com.mateoj.hack2help.util.*;
import com.mateoj.hack2help.util.Error;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by jose on 10/23/15.
 */
public class ApiHelper {
    public static void getToursNearby(LatLng latLng, final Callback<List<Tour>> cb)
    {
        Tour.getTourQuery()
                .findInBackground(new FindCallback<Tour>() {
                    @Override
                    public void done(List<Tour> objects, ParseException e) {
                        if (e != null)
                            cb.error(new Error(e.getLocalizedMessage(), e.getCode()));
                        else
                            cb.done(objects);
                    }
                });
    }
}
