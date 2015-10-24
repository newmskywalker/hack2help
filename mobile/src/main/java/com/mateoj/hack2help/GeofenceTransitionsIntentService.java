package com.mateoj.hack2help;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jose on 10/23/15.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    public static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    public GeofenceTransitionsIntentService() {
        super("geofenceservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "" + geofencingEvent.getErrorCode();
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
//        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
//                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            for (Geofence geo : triggeringGeofences)
            {
                EventBus.getDefault().post(geo);
            }

            // Get the transition details as a String.
            Log.d(TAG, "" + geofenceTransition);
//            String geofenceTransitionDetails = getGeofenceTransitionDetails(
//                    this,
//                    geofenceTransition,
//                    triggeringGeofences
//            );

            // Send notification and log the transition details.
//            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, "" + geofenceTransition);
        } else {
            // Log the error.
            Log.e(TAG, "geofence error");
        }
    }
}
