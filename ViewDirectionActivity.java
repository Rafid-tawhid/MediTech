package com.hk.meditechuser;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hk.meditechuser.Helper.FetchURL;
import com.hk.meditechuser.Helper.TaskLoadedCallback;
import com.hk.meditechuser.Remote.GoogleApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewDirectionActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private Marker marker;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private Polyline polyline;
    private GoogleApiService mService;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_direction);


        //progress dialogue set up
        progressDialog = new AlertDialog.Builder(ViewDirectionActivity.this).create();
        final View view = LayoutInflater.from(ViewDirectionActivity.this).inflate(R.layout.progress_diaog_layout, null);
        progressDialog.setCancelable(false);
        progressDialog.setView(view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mService = Common.getGoogleApiServiceScalars();

        //location
        progressDialog.show();

        buildLocationRequest();
        buildLocationCallback();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
            }
        }


    }

    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (marker != null)
                    marker.remove();

                mLocation = locationResult.getLastLocation();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                        .title(getAddressFromLocation(mLocation.getLatitude(), mLocation.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                marker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));



                //destination
                LatLng destinationLtLng = new LatLng(
                        Double.parseDouble(Common.currentResults.getGeometry().getLocation().getLat()),
                        Double.parseDouble(Common.currentResults.getGeometry().getLocation().getLng())
                );

                mMap.addMarker(new MarkerOptions()
                        .position(destinationLtLng)
                        .title(Common.currentResults.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );


                new FetchURL(ViewDirectionActivity.this).execute(getUrl(mLocation, Common.currentResults.getGeometry().getLocation(), "driving"), "driving");

                progressDialog.dismiss();

            }
        };
    }

    private String getAddressFromLocation(double latitiude, double longitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitiude, longitude, 1);
            if (addresses.size() > 0) {

                address = addresses.get(0).getAddressLine(0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                mLocation = location;
                if (marker != null)
                    marker.remove();

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                        .title(getAddressFromLocation(mLocation.getLatitude(), mLocation.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                marker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                progressDialog.dismiss();


                //destination
                LatLng destinationLtLng = new LatLng(
                        Double.parseDouble(Common.currentResults.getGeometry().getLocation().getLat()),
                        Double.parseDouble(Common.currentResults.getGeometry().getLocation().getLng())
                );

                mMap.addMarker(new MarkerOptions()
                        .position(destinationLtLng)
                        .title(Common.currentResults.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );


                new FetchURL(ViewDirectionActivity.this).execute(getUrl(mLocation, Common.currentResults.getGeometry().getLocation(), "driving"), "driving");

                progressDialog.dismiss();
            }
        });

    }


    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }


    private String getUrl(Location mLocation, com.hk.meditechuser.Model.Location location, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + mLocation.getLatitude() + "," + mLocation.getLongitude();
        // Destination of route
        String str_dest = "destination=" + location.getLat() + "," + location.getLng();
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.maps_direction_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null)
            polyline.remove();
        polyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void goBack(View view) {
        finish();
    }
}
