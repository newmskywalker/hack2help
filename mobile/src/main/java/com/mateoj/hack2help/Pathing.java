package com.mateoj.hack2help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.mateoj.hack2help.data.model.Node;
import com.mateoj.hack2help.util.LocationUtils;

public class Pathing {
    private GoogleMap googleMap;
    private int color;
    private String apikey;

    Pathing(GoogleMap googlemap, int color, String apikey) {
        this.googleMap = googlemap;
        this.apikey = apikey;
        this.color = color;
    }

    public void addRoutes(List<Node> points)
    {
        for (int i = 0; i < points.size() - 1; i++) {
            LatLng o = LocationUtils.geoPointToLatLng(points.get(i).getLocation());
            LatLng d = LocationUtils.geoPointToLatLng(points.get(i + 1).getLocation());
            String url = getMapsApiDirectionsUrl(o, d);
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);
        }
    }

    private String getMapsApiDirectionsUrl(LatLng o, LatLng d) {
        String waypoints = "waypoints=optimize:true";
        String origin = "origin=" + o.latitude + "," + o.longitude;
        String destination = "destination=" + d.latitude + "," + d.longitude;
        String apikey = "key=" + this.apikey;

        String sensor = "sensor=false";
        String params = apikey + "&" + origin + "&" + destination + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());

            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(15);
                polyLineOptions.color(color);
            }

            try {
                if (googleMap != null) {
                    googleMap.addPolyline(polyLineOptions);
                }
            } catch (Exception e) {
            }
        }
    }
}
