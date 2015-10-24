package com.mateoj.hack2help;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mateoj.hack2help.data.model.Node;
import com.mateoj.hack2help.data.model.Tour;
import com.mateoj.hack2help.util.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class TourDetailActivity extends LocationActivity implements OnMapReadyCallback {

    public static final String EXTRA_TOUR = "tour";
    public static final String TAG = TourDetailActivity.class.getSimpleName();

    SupportMapFragment mapFragment;

    Marker currentMarker;
    private boolean isMapReady = false;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton startBtn;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.description)
    TextView description;

    @Bind(R.id.duration)
    TextView duration;

    @Bind(R.id.distance)
    TextView distance;

    @Bind(R.id.image)
    ImageView thumImage;

    @Bind(R.id.nodes)
    LinearLayout nodesLayout;

    private Tour mTour;
    private List<Node> mNodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        mTour = EventBus.getDefault().getStickyEvent(Tour.class);

        setSupportActionBar(toolbar);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(mTour);
                startActivity(new Intent(TourDetailActivity.this, TourPlaybackActivity.class));
            }
        });
        toolbarLayout.setTitle(mTour.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initMap();
        initLayout();
        fetchNodes();
    }

    private void fetchNodes()
    {
        mTour.getNodes(new Callback<List<Node>>() {
            @Override
            public void done(List<Node> result) {
                initNodes(result);
            }

            @Override
            public void error(com.mateoj.hack2help.util.Error error) {

            }
        });
    }

    private void initNodes(List<Node> nodes)
    {
        mNodes = nodes;
        nodesLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for (Node node : nodes)
        {
            View view = inflater.inflate(R.layout.row_node, nodesLayout, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView title = (TextView) view.findViewById(R.id.title);
            if (!node.getThumbUrl().equals(""))
                Picasso.with(this)
                        .load(node.getThumbUrl())
                        .into(imageView);
            title.setText(node.getTitle());
            nodesLayout.addView(view);
        }

    }

    private void initLayout()
    {
        title.setText(mTour.getTitle());
        description.setText(mTour.getDescription());
        distance.setText(String.format(getString(R.string.distance),
                NumberFormat.getInstance().format(mTour.getDistance())));
        duration.setText(String.format(getString(R.string.duration),
                NumberFormat.getInstance().format(mTour.getDuration())));
        if (mTour.getThumbUrl().equals(""))
            Picasso.with(this)
            .load(R.drawable.placeholder)
            .into(thumImage);
        else
            Picasso.with(this)
                    .load(mTour.getThumbUrl())
                    .into(thumImage);

    }

    private void initMap()
    {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;

        LatLng here = new LatLng(36.8475, -76.2913);
        currentMarker = googleMap.addMarker(new MarkerOptions().position(here).title("Me"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(here));
    }


    @Override
    public void onLocationChanged(Location location) {
        if (isMapReady)
            currentMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }
}
