package com.worshipsearcher.activities;

/**
 * Created by Mihaly on 2015.04.24..
 */


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;


import java.util.ArrayList;

import com.worshipsearcher.R;
import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Worship;

public class WorshipListActivity extends ListActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.acticity_worshiplist);
        Intent intent = getIntent();
        ArrayList<Worship> worshipList = (ArrayList<Worship>) intent.getSerializableExtra("worshiplist");
        Church selectedchurch = (Church) intent.getSerializableExtra("selectedchurch");
        String type = intent.getStringExtra("type");
        WorshipAdapter worshipAdapter =
                new WorshipAdapter(getApplicationContext(), worshipList, selectedchurch, type);
        setListAdapter(worshipAdapter);
        int background = ContextCompat.getColor(this, R.color.colorBackground);
        getListView().setBackgroundColor(background);

    }

}
