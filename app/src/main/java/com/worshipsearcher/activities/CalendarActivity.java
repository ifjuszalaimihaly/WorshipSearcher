package com.worshipsearcher.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.worshipsearcher.R;
import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.ChurchWorship;
import com.worshipsearcher.entities.Worship;
import com.worshipsearcher.services.DateListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CalendarActivity extends Activity {

    private Calendar calendar = Calendar.getInstance();
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        WorshipSearcher.getDbl().open();
        final ArrayList<Worship> worships = WorshipSearcher.getDbl().getAllWroship();
        final ArrayList<ChurchWorship> churchWorships = new ArrayList<>();
        final Location location = MainActivity.getPlace();
        //Log.d("sh",location.toString());
        for(Worship worship: worships){
            Church church = WorshipSearcher.getDbl().getChurchById(worship.getChurchid());
            church.setDistance(location);
            Log.d("sh",church.toString());
            ChurchWorship churchWorship = new ChurchWorship(church,worship);
            churchWorships.add(churchWorship);

        }
        WorshipSearcher.getDbl().close();
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                CalendarActivity.this.year=year;
                CalendarActivity.this.month=month;
                CalendarActivity.this.day=dayOfMonth;
                month++;
            }
        });
        Button button = (Button) findViewById(R.id.datesetButton);
        button.setText("Keresés");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(year, month, day);
                if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
                    //Log.d("sh", "date seted");
                    /*WorshipSearcher.getDbl().open();
                    ArrayList<Worship> worships = WorshipSearcher.getDbl().getAllWroship();
                    WorshipSearcher.getDbl().close();*/
                    ArrayList goodChurchWorships = new ArrayList();
                    DateListener dateListener = new DateListener();
                    month=month+1;
                    String yearString = String.valueOf(year);
                    String monthString = String.valueOf(month);
                    if(month<10){
                        monthString = "0"+month;
                    }
                    String dayString = String.valueOf(day);
                    if(day<10){
                        dayString = "0"+day;
                    }
                    String date= yearString+ "." + monthString + "." + dayString;
                    for (ChurchWorship churchWorship : churchWorships) {
                        churchWorship.getWorship().setDate(date);
                        //Log.d("sh",worship.toString());
                        //Log.d("sh", "month " + month + " day " + day + "weekid" + worship.getWeekid());
                        if (dateListener.isGoodDay(month, day, churchWorship.getWorship().getWeekid())) {
                           goodChurchWorships.add(churchWorship);
                        }
                    }


                    Intent intent = new Intent();
                    //intent.putExtra("date", date);
                    Collections.sort(goodChurchWorships);
                    for(ChurchWorship churchWorship: churchWorships){
                        Log.d("sh",churchWorship.getChurch().getDistanceString());
                    }
                    intent.putExtra("worships", goodChurchWorships);
                    //Log.d("sh", intent.toString());
                    intent.setClass(CalendarActivity.this, DayWorshipActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Nincs Istetisztelet a kiválasztott napon", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
