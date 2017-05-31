
package com.worshipsearcher.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.worshipsearcher.R;
import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Worship;
import com.worshipsearcher.services.DateListener;

import java.util.ArrayList;
import java.util.Collections;

public class MoreChurchMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<Church> churches;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_church_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        WorshipSearcher.getDbl().open();
        churches = WorshipSearcher.getDbl().getAllChurch();
        WorshipSearcher.getDbl().close();
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
        mMap.setOnMarkerClickListener(this);
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Log.e("debug", "normal");
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4f));
            }
        }
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Log.e("debug", "normal");
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f));
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
            }
        }
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Log.e("debug", "large");
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7f));
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f));
            }

        }
        for (Church church : churches) {
            if (church.getLatitude() == church.getLongitude()) {
                Log.d("sh", church.toString());
            }
            LatLng latLng = new LatLng(church.getLatitude(), church.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(church.getCity() + ", " + church.getAddress());
            Integer churchid = church.getChurchid();
            mMap.addMarker(markerOptions);
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.d("sh", "marker click");
        Toast.makeText(context, marker.getTitle(), Toast.LENGTH_LONG).show();
        String markerTitle = marker.getTitle();
        String city = WorshipSearcher.slice(markerTitle, ',');
        String address = WorshipSearcher.slice2(markerTitle, ',');
        Log.d("sh", "city ;" + city + ";");
        Log.d("sh", "adress ;" + address + ";");
        WorshipSearcher.getDbl().open();
        final int churchid = WorshipSearcher.getDbl().getChurchIdByCityAddress(city, address);
        final Church selectedChurch = WorshipSearcher.getDbl().getChurchById(churchid);
        Log.d("sh", selectedChurch.toString());
        WorshipSearcher.getDbl().close();
        Log.d("sh", ";" + city + ";");
        Log.d("sh", ";" + address + ";");
        String[] options = new String[]{"Legközelbbi alkalom", "Kép a templomról", "A templom alkalmai havonta"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Válassza ki az agrregáció típusát");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    WorshipSearcher.getDbl().open();
                    ArrayList<Worship> worshipList = WorshipSearcher.getDbl().getWorshipByChurch(selectedChurch.getChurchid());
                    if (worshipList.size() != 0) {
                        DateListener dateListener = new DateListener();
                        for (Worship w : worshipList) {
                            w.setDate(dateListener.searchForGoodDay(w.getWeekid(), 1));
                        }
                        Collections.sort(worshipList);
                        Worship worship = worshipList.get(0);
                        ArrayList<Worship> worships = new ArrayList<>();
                        for (Worship worship1 : worshipList) {
                            if (worship1.getDate().equals(worship.getDate())) {
                                worships.add(worship1);
                            }
                        }
                        WorshipSearcher.getDbl().close();

                        Intent intent = new Intent();
                        intent.putExtra("type", "dayview");
                        intent.putExtra("worshiplist", worships);
                        intent.putExtra("selectedchurch", selectedChurch);
                        intent.setClass(MoreChurchMapsActivity.this, WorshipListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nincs Istentisztelet a kiválasztott templomban", Toast.LENGTH_LONG).show();
                    }
                }
                if (which == 1) {
                    Intent intent = new Intent();
                    Log.d("sh", "churchid " + churchid);
                    intent.putExtra("churchid", churchid);
                    intent.setClass(MoreChurchMapsActivity.this, ChurchImageActivity.class);
                    Toast.makeText(getApplicationContext(),"A kép letöltése folyamatban",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                if (which == 2) {
                    WorshipSearcher.getDbl().open();
                    ArrayList<Worship> worshipList =
                            WorshipSearcher.getDbl().getWorshipByChurch(selectedChurch.getChurchid());
                    WorshipSearcher.getDbl().close();
                    if (worshipList.size() != 0) {
                        Intent intent = new Intent();
                        Log.d("sh", "2 " + selectedChurch.toString());
                        intent.putExtra("selectedchurch", selectedChurch);
                        intent.putExtra("worshiplist", worshipList);
                        intent.putExtra("type", "monthview");
                        intent.setClass(MoreChurchMapsActivity.this, WorshipListActivity.class);
                        ;
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nincs Istentisztelet a kiválasztott templomban", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

}
