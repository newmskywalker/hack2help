package com.mateoj.hack2help;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mateoj.hack2help.data.model.Node;
import com.mateoj.hack2help.data.model.Tour;
import com.mateoj.hack2help.util.Callback;
import com.mateoj.hack2help.util.LocationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class TourPlaybackActivity extends LocationActivity implements OnMapReadyCallback, ResultCallback {

    SupportMapFragment mapFragment;
    private boolean isMapReady = false;
    private List<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private List<Node> mNodes;
    private MediaPlayer mediaPlayer;
    private Marker currentMarker;
    WifiManager.WifiLock wifiLock;

    private Tour mTour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_playback);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        mTour = EventBus.getDefault().getStickyEvent(Tour.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        LatLng here = new LatLng(36.8475, -76.2913);
        currentMarker = googleMap.addMarker(new MarkerOptions().position(here).title("Me"));
        currentMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_24dp));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 15));
        drawMarks(googleMap);
    }


    private GeofencingRequest getGeoFencingRequest()
    {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private void onPlayNode(final Node node)
    {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        }

        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(node.getAudioUrl());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    wifiLock.release();
                }
            });
            wifiLock.acquire();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(android.R.id.content), node.getTitle(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void onEvent(Geofence geofence)
    {
        for (Geofence fence : mGeofenceList)
        {
            if (fence.getRequestId().equals(geofence.getRequestId()))
            {
                for (final Node node : mNodes) {
                    if (node.getObjectId().equals(fence.getRequestId())){
                        onPlayNode(node);
                    }
                }
            }
        }
    }

    private List<String> getFenceStringList()
    {
        List<String> list = new ArrayList<>();
        for (Node node : mNodes)
            list.add(node.getObjectId());
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearFences();
        mediaPlayer = null;
    }

    private void clearFences() {
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, getFenceStringList());
    }


    private void setUpGeofences()
    {
        mTour.getNodes(new Callback<List<Node>>() {
            @Override
            public void done(List<Node> result) {
                mGeofenceList = new ArrayList<Geofence>();
                for (Node node : result) {
                    mGeofenceList.add(new Geofence.Builder()
                            .setRequestId(node.getObjectId())
                            .setCircularRegion(
                                    node.getLocation().getLatitude(),
                                    node.getLocation().getLongitude(),
                                    50)
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build());
                }
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        getGeoFencingRequest(),
                        getGeofencePendingIntent()
                ).setResultCallback(TourPlaybackActivity.this);

            }

            @Override
            public void error(com.mateoj.hack2help.util.Error error) {

            }
        });

    }

    private void drawMarks(final GoogleMap googleMap)
    {
        mTour.getNodes(new Callback<List<Node>>() {
            @Override
            public void done(List<Node> result) {
                mNodes = result;
                if (result.size() > 0)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LocationUtils.geoPointToLatLng(result.get(0).getLocation()), 12));
                for (Node node : result)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(LocationUtils.geoPointToLatLng(node.getLocation()))
                            .title(node.getTitle()));
                }
            }

            @Override
            public void error(com.mateoj.hack2help.util.Error error) {

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (isMapReady) {
            setUpGeofences();
        }
    }

    @Override
    public void onResult(Result result) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentMarker != null)
            currentMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }
}
