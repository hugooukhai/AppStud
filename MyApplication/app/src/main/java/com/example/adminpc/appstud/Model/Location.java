package com.example.adminpc.appstud.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by adminPC on 18/05/2017.
 */

public class Location {

    public double lat;
    public double lng;

    public LatLng getLatLng(){
        return new LatLng(lat,lng);
    }

}
