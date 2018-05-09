package com.brawijaya.filkom.restpedia.utils;

import android.graphics.Color;
import android.util.Log;

import com.brawijaya.filkom.restpedia.network.model.LegsResponse;
import com.brawijaya.filkom.restpedia.network.model.PolylineResponse;
import com.brawijaya.filkom.restpedia.network.model.RouteResponse;
import com.brawijaya.filkom.restpedia.network.model.StepsResponse;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public final class MapUtils {

    private MapUtils() {
        // Class is not publicly instantiate
    }

    public static LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    public static void addCameraToMap(GoogleMap googleMap, LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static void drawRouteOnMap(GoogleMap map, List<LatLng> directions){
        Log.d("MapUtils", "drawRouteOnMap");
        PolylineOptions options = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);
        options.addAll(directions);
        map.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(directions.get(1).latitude, directions.get(1).longitude))
                .zoom(17).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static List<LatLng> getDirectionPolyline(List<RouteResponse> routes){
        List<LatLng> directionList = new ArrayList<>();
        for(RouteResponse route : routes){
            List<LegsResponse> legs = route.getLegs();
            for(LegsResponse leg : legs){
                List<StepsResponse> steps = leg.getSteps();
                for(StepsResponse step : steps){
                    PolylineResponse polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePolyline(points);
                    directionList.addAll(singlePolyline);
                }
            }
        }
        return directionList;
    }

    private static List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
