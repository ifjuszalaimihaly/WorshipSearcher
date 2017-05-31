package com.worshipsearcher.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.ChurchWorship;
import com.worshipsearcher.entities.Worship;

/**
 * Created by Mihaly on 2015.11.09..
 */
public class DayWorshipActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ArrayList<ChurchWorship> worshipList = (ArrayList<ChurchWorship>) intent.getSerializableExtra("worships");
        /*for (Worship worship: worshipList) {
            worship.setDate(intent.getStringExtra("date"));
        }*/
        DayWorshipAdapter dayWorshipAdapter = new DayWorshipAdapter(worshipList);
        setListAdapter(dayWorshipAdapter);
        getListView().setBackgroundColor(Color.rgb(58, 139, 189));
    }




}
