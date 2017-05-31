package com.worshipsearcher.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.worshipsearcher.R;
import com.worshipsearcher.dbloader.WorshipSearcher;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.ChurchWorship;
import com.worshipsearcher.entities.Worship;

/**
 * Created by Mihaly on 2015.11.09..
 */
public class DayWorshipAdapter extends BaseAdapter {

    public final ArrayList<ChurchWorship> churchWorshipList;
    @Override
    public int getCount() {
        return churchWorshipList.size();
    }

    @Override
    public Object getItem(int position) {
        return churchWorshipList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public DayWorshipAdapter(ArrayList<ChurchWorship> churchWorshipList) {
        this.churchWorshipList = churchWorshipList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Worship worship = churchWorshipList.get(position).getWorship();
        final Church church = churchWorshipList.get(position).getChurch();
        WorshipSearcher.getDbl().open();
        String week = WorshipSearcher.getDbl().getWeekById(worship.getWeekid());
        WorshipSearcher.getDbl().close();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewItem = inflater.inflate(R.layout.dayworship, null);
        TextView textViewDate =(TextView) viewItem.findViewById(R.id.date);
        textViewDate.setText(worship.getDate()+ " " + worship.getTermin());
        TextView textViewCity = (TextView) viewItem.findViewById(R.id.churchCity2);
        textViewCity.setText(church.getCity()+" "+church.getAddress());
        TextView textViewChurchComment = (TextView) viewItem.findViewById(R.id.churchComment);
        textViewChurchComment.setText(church.getComment());
        TextView textViewWeekComment = (TextView) viewItem.findViewById(R.id.weekcommnet2);
        textViewWeekComment.setText(week);
        TextView textViewComment = (TextView) viewItem.findViewById(R.id.worshipcomment);
        textViewComment.setText(worship.getComment());
        return viewItem;
    }
}
