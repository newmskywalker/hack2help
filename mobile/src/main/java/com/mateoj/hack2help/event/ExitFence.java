package com.mateoj.hack2help.event;

import com.google.android.gms.location.Geofence;

import java.util.List;

/**
 * Created by jose on 10/24/15.
 */
public class ExitFence {
    private List<Geofence> geofences;

    public ExitFence(List<Geofence> geofences)
    {
        this.geofences = geofences;
    }

    public List<Geofence> getGeofences() {
        return geofences;
    }
}
