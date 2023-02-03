package com.uber.app.team23.AirRide.Utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.uber.app.team23.AirRide.model.rideData.Location;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapUtils {

//    private static final String LOCAL_STORAGE = "C:/Users/Obrad/Desktop/Google/api_key.txt";
    private static final String LOCAL_STORAGE = "C:/Users/danic/OneDrive/Desktop/api_key.txt";
    private static String API_KEY;

    public static Location getLocationAtTime(double lat1, double lng1, double lat2, double lng2) {
        DirectionsResult direction = getDirection(lat1, lng1, lat2, lng2);
        DirectionsResult inverse = getDirection(lat2, lng2, lat1, lng1);
        EncodedPolyline overviewPolyline = direction.routes[0].overviewPolyline;
        List<LatLng> cords = overviewPolyline.decodePath();
        long distanceInMeters = direction.routes[0].legs[0].distance.inMeters;
        long distanceInMetersInverse = inverse.routes[0].legs[0].distance.inMeters;
        if (cords.size() > 1 && distanceInMeters >= 100 && distanceInMetersInverse >= 100) {
            LatLng loc = cords.get(1);
            return new Location(loc.lat, loc.lng);
        }
        return new Location(lat2, lng2);
    }

    public static DirectionsResult getDirection(double lat1, double lng1, double lat2, double lng2) {
        try {
            API_KEY = new String(Files.readAllBytes(Paths.get(LOCAL_STORAGE)));
            System.err.println("KEY: " + API_KEY);
            List<LatLng> path = new ArrayList<>();
            GeoApiContext context = new GeoApiContext.Builder().
                    apiKey(API_KEY).build();

            DirectionsApiRequest req = DirectionsApi.getDirections(context,
                    lat1 +","+lng1, lat2+","+lng2);
            DirectionsResult res = req.await();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
