package com.worshipsearcher.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.worshipsearcher.R;
import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Worship;
import com.worshipsearcher.services.DateListener;
import com.worshipsearcher.services.ImageDownLoader;

public class CityActivity extends Activity {
    private ArrayList<Church> churchList = new ArrayList<Church>();
    private String[] menuItems = new String[]{"Legközelbbi alkalom", "Kép a templomról", "Templom a térképen"};
    private String[] comp_array;
    private ArrayAdapter<String> comp_adapter;
    private double latitude;
    private double longitude;

    public void onCreate(Bundle savedinstanceState) {

        super.onCreate(savedinstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        final Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        setContentView(R.layout.city);
        Thread t = new Thread() {
            public void run() {
                WorshipSearcher.getDbl().open();
                churchList = WorshipSearcher.getDbl().getAllChurch();
                int i = 0;
                comp_array = new String[churchList.size()];
                for (Church church : churchList) {
                    church.setDistance(location);
                    comp_array[i] = church.getCity() + " " + church.getAddress();
                    Log.d("sh", comp_array[i].toString());
                    i++;
                    Log.d("sh", "distance: " + church.getDistance());

                }
                Collections.sort(churchList);
                WorshipSearcher.getDbl().close();
            }
        };
        Log.d("sh", location.toString());
        t.run();
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        TextView textView = (TextView) findViewById(R.id.suggestionList);
        comp_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.autocompletetextview, R.id.suggestionList, comp_array);
        ListView listViewChurhes = (ListView) findViewById(R.id.churchList);
        Log.d("sh", "Listahoszz:" + ((Integer) churchList.size()).toString());
        final ChurchAdapter churchAdapter = new ChurchAdapter(getApplicationContext(), R.layout.churchrow, churchList);
        listViewChurhes.setAdapter(churchAdapter);
        autoCompleteTextView.setAdapter(comp_adapter);
        listViewChurhes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Church seletctedChurch = (Church) churchAdapter.getItem(position);
                WorshipSearcher.getDbl().open();
                ArrayList<Worship> worshipList =
                        WorshipSearcher.getDbl().getWorshipByChurch(seletctedChurch.getChurchid());
                WorshipSearcher.getDbl().close();
                if (worshipList.size() != 0) {
                    Intent intent = new Intent();
                    intent.putExtra("selectedchurch", seletctedChurch);
                    intent.putExtra("worshiplist", worshipList);
                    intent.putExtra("type", "monthview");
                    intent.setClass(CityActivity.this, WorshipListActivity.class);
                    ;
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Nincs Istentisztelet a kiválasztott templomban", Toast.LENGTH_LONG).show();
                }


            }

            ;
        });
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //autoCompleteTextView.showDropDown();
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                int pos = Arrays.asList(comp_array).indexOf(selected);
                int space = selected.indexOf(" ");
                String city = selected.substring(0, space);
                String address = selected.substring(space + 1, selected.length());
                WorshipSearcher.getDbl().open();
                int churchid = WorshipSearcher.getDbl().getChurchIdByCityAddress(city, address);
                Church selectedChurch = WorshipSearcher.getDbl().getChurchById(churchid);
                ArrayList<Worship> worshipList =
                        WorshipSearcher.getDbl().getWorshipByChurch(churchid);
                WorshipSearcher.getDbl().close();
                Toast.makeText(getApplication(), selected, Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.putExtra("type", "monthview");
                intent.putExtra("selectedchurch", selectedChurch);
                intent.putExtra("worshiplist", worshipList);
                intent.setClass(CityActivity.this, WorshipListActivity.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(listViewChurhes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.churchList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(churchList.get(info.position).getCity());
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];
        Church selectedChurch = churchList.get(info.position);
        int churchid = selectedChurch.getChurchid();
        Intent intent = getIntent();
        ;
        if (item.getTitle().equals(menuItems[0])) {
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
                intent.putExtra("type", "dayview");
                intent.putExtra("worshiplist", worships);
                intent.putExtra("selectedchurch", selectedChurch);
                intent.setClass(CityActivity.this, WorshipListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Nincs Istentisztelet a kiválasztott templomban", Toast.LENGTH_LONG).show();
            }
        }
        if (item.getTitle().equals((menuItems[1]))) {
            intent.setClass(CityActivity.this, ChurchImageActivity.class);
            Log.d("sh", "churchid " + churchid);
            intent.putExtra("churchid", churchid);
            Toast.makeText(getApplicationContext(),"A kép letöltése folyamatban",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        if (item.getTitle().equals(menuItems[2])) {
            String address = selectedChurch.getAddress();
            address = address.replace(' ', '+').trim();
            String city = selectedChurch.getCity().trim();
            String uri = "https://www.google.hu/maps/place/" + address + ",+" + city + ",+/@" + selectedChurch.getLatitude() + "," + selectedChurch.getLongitude();
            intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
            startActivity(intent);
        }
        return true;
    }
}
