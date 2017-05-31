package com.worshipsearcher.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.worshipsearcher.dbloader.ChurchDownloadTask;
import com.worshipsearcher.dbloader.DbLoader;
import com.worshipsearcher.dbloader.WorshipSearcher;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.worshipsearcher.R;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Worship;


/**
 * Created by Mihaly on 2015.05.01..
 */
public class MainActivity extends Activity {

    private Button buttonMorePlaces;
    private Button buttonPalces;
    private Button buttonDates;
    private static Location location;
    private double latitude;
    private double longitude;

    private LocationManager locationManager;
    private LocationListener locationListener;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*WorshipSearcher.getDbl().open();
        WorshipSearcher.getAllChurches();
        WorshipSearcher.getAllWorships();
        WorshipSearcher.getDbl().close();*/
        buttonMorePlaces = (Button) findViewById(R.id.buttonMorePlaces);
        buttonPalces = (Button) findViewById(R.id.buttonPlaces);
        buttonDates = (Button) findViewById(R.id.buttonDates);
        longitude = 0.0;
        latitude = 0.0;
        ImageView refreshView = (ImageView) findViewById(R.id.refresh);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Az adatok újratöltése folyamatban",Toast.LENGTH_LONG).show();
                WorshipSearcher.getDbl().open();
                WorshipSearcher.getDbl().deleteAllDataFromDb();
                WorshipSearcher.getAllChurches();
                WorshipSearcher.getAllWeeks();
                WorshipSearcher.getAllWorships();
                WorshipSearcher.closeConnection();
                Toast.makeText(getApplicationContext(),"Az adatok letöltése befejeződött",Toast.LENGTH_LONG).show();
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setMaxWidth(android.R.attr.width);
        imageView.setMinimumWidth(android.R.attr.width);
        final ImageView infoView = (ImageView) findViewById(R.id.infoView);
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MainActivity.this.location = location;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();
                //Log.d("sh",location.toString());
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
        };
        getLocation();
        //Toast.makeText(this,"Az adatok letöltése folyamatban van",Toast.LENGTH_LONG).show();
        //while (!WorshipSearcher.finished){
            //+Log.d("sh","waiting");
        //}
        //Toast.makeText(this,"Az adatok letöltése befejeződött",Toast.LENGTH_LONG).show();
        //if(WorshipSearcher.isdDataInDb()) {
            buttonMorePlaces.setOnClickListener(new ButtonMorePlacesClickListener());
            buttonPalces.setOnClickListener(new ButtonPalcesClickListener());
            buttonDates.setOnClickListener(new ButtonDatesClickListener());
        //}


    }

    public static Location getPlace(){
        return MainActivity.location;
    }

    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            locationManager.requestLocationUpdates("gps", 500, 0, locationListener);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }


    class ButtonMorePlacesClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MoreChurchMapsActivity.class);
            startActivity(intent);
        }
    }

    class ButtonPalcesClickListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.setClass(MainActivity.this, CityActivity.class);
            startActivity(intent);
        }
    }

    class ButtonDatesClickListener implements View.OnClickListener {
        public void onClick(View v) {
           Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.setClass(getApplicationContext(),CalendarActivity.class);
            startActivity(intent);
        }
    }
}