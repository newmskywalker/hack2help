package com.mateoj.hack2help.util;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

/**
 * Created by jose on 10/23/15.
 */
public class LocationUtils {
    public static LatLng geoPointToLatLng(ParseGeoPoint geoPoint)
    {
        return new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    public static ParseGeoPoint latLngToParseGeo(LatLng latLng)
    {
        return new ParseGeoPoint(latLng.latitude, latLng.longitude);
    }
}
