package com.worshipsearcher.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.worshipsearcher.R;
import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Worship;

/**
 * Created by Mihaly on 2015.04.24..
 */
public class WorshipAdapter extends BaseAdapter {

    private final ArrayList<Worship> worshipList;
    private final Church selectedChurch;
    private final String type;

    public WorshipAdapter(final Context context, final ArrayList<Worship> worshipList, Church selectedChurch, String type) {
        this.worshipList = worshipList;
        this.selectedChurch = selectedChurch;
        this.type = type;
    }

    @Override
    public int getCount() {
        return worshipList.size();
    }

    @Override
    public Object getItem(int position) {
        return worshipList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Worship worship = worshipList.get(position);
        WorshipSearcher.getDbl().open();
        String week = WorshipSearcher.getDbl().getWeekById(worship.getWeekid());
        WorshipSearcher.getDbl().close();
        View viewItem = null;
        if (type.equals("monthview")) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItem = inflater.inflate(R.layout.worship, null);
            TextView textViewTermin = (TextView) viewItem.findViewById(R.id.termin);
            textViewTermin.setText(worship.getTermin());
            TextView textViewComment = (TextView) viewItem.findViewById(R.id.comment);
            textViewComment.setText(worship.getComment());
            TextView textViewCity = (TextView) viewItem.findViewById(R.id.churchcomment2);
            textViewCity.setText(selectedChurch.getCity());
            TextView textViewWeekComment = (TextView) viewItem.findViewById(R.id.weekcommnet);
            Log.d("sh", week);
            textViewWeekComment.setText(week);
        }
        if(type.equals("dayview")){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItem = inflater.inflate(R.layout.simpleworship,null);
            TextView dateText = (TextView) viewItem.findViewById(R.id.date);
            dateText.setText(worshipList.get(position).getDate() + " " + worshipList.get(position).getTermin());
            TextView churchText = (TextView) viewItem.findViewById(R.id.churchCity2);
            churchText.setText(selectedChurch.getCity()+", "+ selectedChurch.getAddress());
            TextView weekText = (TextView) viewItem.findViewById(R.id.weekcommnet2);
            weekText.setText(week);
            TextView worshipCommentText = (TextView) viewItem.findViewById(R.id.worshipcomment);
            worshipCommentText.setText(worship.getComment());
        }
        return viewItem;
    }
}
