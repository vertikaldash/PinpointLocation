package com.dash.vertikal.locationtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Fathur on 3/8/2016.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context context;

    boolean flagGPS = false;
    boolean flagInternet = false;
    boolean flagGetLocation = false;

    double latitude;
    double longitude;

    private static final long JARAK_MIN_UPDATE = 100;
    private static final long WAKTU_MIN_UPDATE = 1000 * 60 * 1;

    protected LocationManager locationManager;

    Location location;

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            flagGPS = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);

            flagInternet = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if (!flagGPS && !flagInternet) {

            } else {
                flagGetLocation = true;

                if (flagInternet) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, WAKTU_MIN_UPDATE, JARAK_MIN_UPDATE, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (flagGPS) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, WAKTU_MIN_UPDATE, JARAK_MIN_UPDATE, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean isFlagGetLocation(){
        return this.flagGetLocation;
    }

    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS harus menyala");

        alertDialog.setMessage("GPS anda tidak aktif, apakah anda ingin mengaktifkannya?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_setting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent_setting);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
