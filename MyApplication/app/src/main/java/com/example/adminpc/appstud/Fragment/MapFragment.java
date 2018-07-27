package com.example.adminpc.appstud.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminpc.appstud.Model.Places;
import com.example.adminpc.appstud.Model.Results;
import com.example.adminpc.appstud.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by adminPC on 18/05/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker sydneyMarker;
    private Marker myPositionMarker;
    PlaceAutocompleteFragment placeAutoComplete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeAutoComplete = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("OnviewCreated", "");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        sydneyMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void updateMap(LatLng latLng) {
        sydneyMarker.remove();
        LatLng myPosition = latLng;
        myPositionMarker = mMap.addMarker(new MarkerOptions().position(myPosition).title("Default Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LocationManager lm = (LocationManager)getContext().getSystemService(getContext().LOCATION_SERVICE);
        boolean gps_enabled = false;
        //check if location is enabled on the phone
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           if(gps_enabled) {
               Log.d("gps", "enabled: ");
               myPositionMarker.remove();
           }
               //add  the blue dot on my localisation
               mMap.setMyLocationEnabled(true);

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,14));

    }

    public void addMarkers(Places places, LatLng mLatLng) {
        mMap.clear();
        LatLng myPosition = mLatLng;
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Default Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        for (Results result : places.results) {
            LatLng placeLatLng = result.geometry.location.getLatLng();
            mMap.addMarker(new MarkerOptions().position(placeLatLng).title(result.name));

        }
    }

    public void addMarkers(Places places){
            mMap.clear();
            for(Results result: places.results){
                LatLng placeLatLng = result.geometry.location.getLatLng();
                mMap.addMarker(new MarkerOptions().position(placeLatLng).title(result.name));

            }
    }
}
