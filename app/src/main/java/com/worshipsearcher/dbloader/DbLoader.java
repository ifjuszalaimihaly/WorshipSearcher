package com.worshipsearcher.dbloader;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.worshipsearcher.dbloader.DatabaseHelper;
import com.worshipsearcher.dbloader.DbConstants;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Week;
import com.worshipsearcher.entities.Worship;

/**
 * Created by Mihaly on 2015.04.24..
 */
public class DbLoader {
    private Context ctx;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase mdb;


    public DbLoader(Context ctx) {
        this.ctx = ctx;

    }

    public void open() throws SQLiteException {
        dbHelper = new DatabaseHelper(ctx, DbConstants.DATABASE_NAME);
        mdb = dbHelper.getWritableDatabase();
        dbHelper.onCreate(mdb);
    }

    public void close() {
        dbHelper.close();
    }


    public void createChurch(Church church) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        try {
        ContentValues values = new ContentValues();
        values.put(DbConstants.Churches.CHURCHID, church.getChurchid());
        values.put(DbConstants.Churches.CONID, church.getConid());
        values.put(DbConstants.Churches.CITY, church.getCity());
        values.put(DbConstants.Churches.ADDRESS, church.getAddress());
        values.put(DbConstants.Churches.LAT, church.getLatitude());
        values.put(DbConstants.Churches.LON, church.getLongitude());
        values.put(DbConstants.Churches.COMMENT, church.getComment());
        mdb.insert(DbConstants.Churches.DATABASE_TABLE, null, values);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createWorship(Worship worship) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(DbConstants.Worships.WORSHIPID, worship.getWorshipid());
        values.put(DbConstants.Worships.CHURCHID, worship.getChurchid());
        values.put(DbConstants.Worships.TERMIN, worship.getTermin());
        values.put(DbConstants.Worships.COMMENT, worship.getComment());
        values.put(DbConstants.Worships.WEEKID, worship.getWeekid());
        mdb.insert(DbConstants.Worships.DATABASE_TABLE, null, values);
    }

    public void createWeek(Week week) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.Weeks.WEEKID, week.getWeekid());
            values.put(DbConstants.Weeks.TYPE, week.getType());
            mdb.insert(DbConstants.Weeks.DATABASE_TABLE, null, values);
        } catch (SQLiteException e){
            e.printStackTrace();
            //Log.d("sh","week insert exception");
        }

        }

    public boolean isWorshipExists(int worshipid) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String args[] = new String[1];
        args[0] = new String(String.valueOf(worshipid));
        Cursor c = mdb.rawQuery("SELECT * FROM worships WHERE worshipid = ?", args);
        if (c.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isChurchExits(int churchid){
        Log.d("sh", "churchid" +churchid);
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String args[] = new String[1];
        args[0] = new String(String.valueOf(churchid));
        Cursor c = mdb.rawQuery("SELECT * FROM churches WHERE churchid= ?", args);
        Log.d("sh", "cursor length: " + c.getCount() + c.moveToFirst());
        if (c.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<Church> getAllChurch() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
            ArrayList<Church> churches = new ArrayList<Church>();
            String selectquery = "SELECT * FROM " + DbConstants.Churches.DATABASE_TABLE;
            Cursor c = mdb.rawQuery(selectquery, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    Church church = new Church(c.getShort(0), c.getInt(1), c.getString(2),
                            c.getString(3), c.getFloat(4), c.getFloat(5), c.getString(6));
                    churches.add(church);
                    c.moveToNext();
                }
            }
            c.close();
            return churches;

    }


    public Worship getWorshipById(int worshipId) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        Worship worship = null;
        String selectquery = "SELECT * FROM " + DbConstants.Worships.DATABASE_TABLE + " WHERE " + DbConstants.Worships.WORSHIPID + "=" + worshipId;
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            worship = new Worship(worshipId, c.getShort(1), c.getString(2), c.getShort(3), c.getString(4));
        }
        return worship;
    }

    public ArrayList<Worship> getAllWroship() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        ArrayList<Worship> worships = new ArrayList<Worship>();
        String selectquery = "SELECT * FROM " + DbConstants.Worships.DATABASE_TABLE;
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Worship worship = new Worship(c.getInt(0), c.getShort(1), c.getString(2), c.getShort(3), c.getString(4));
                worships.add(worship);
                c.moveToNext();
            }
        }
        c.close();
        return worships;
    }

    public ArrayList<Week> getAllWeek() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        ArrayList<Week> l = new ArrayList<Week>();
        String selectquery = "SELECT * FROM " + DbConstants.Weeks.DATABASE_TABLE;
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Week h = new Week(c.getShort(0), c.getString(1));
                l.add(h);
                c.moveToNext();
            }
        }
        c.close();
        return l;
    }


    public ArrayList<Worship> getWorshipByChurch(int churchid) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        ArrayList<Worship> l = new ArrayList<Worship>();
        String selectquery = "SELECT * FROM " + DbConstants.Worships.DATABASE_TABLE + " WHERE " + DbConstants.Worships.CHURCHID + "=" + churchid;
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Worship i = new Worship(c.getInt(0), c.getShort(1), c.getString(2), c.getShort(3), c.getString(4));
                l.add(i);
                c.moveToNext();
            }
        }
        c.close();
        return l;
    }

    public String getWeekById(int weekid) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String selectquery =  "SELECT * FROM " + DbConstants.Weeks.DATABASE_TABLE + " WHERE " + DbConstants.Weeks.WEEKID + "=" + weekid;
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            return c.getString(1);
        }
        c.close();
        return "hiba";
    }

    public ArrayList<Integer> getAllChurchIds() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String selectquery = "SELECT * FROM " + DbConstants.Churches.DATABASE_TABLE + ";";
        Cursor c = mdb.rawQuery(selectquery, null);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        while (c.moveToNext()) {
            ids.add(c.getInt(0));
        }
        return ids;
    }

    public Church getChurchById(int churchid) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String selectquery = "SELECT * FROM " + DbConstants.Churches.DATABASE_TABLE + " WHERE " + DbConstants.Churches.CHURCHID + "=" + churchid;
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            return new Church(c.getShort(0), c.getInt(1), c.getString(2), c.getString(3), c.getLong(4), c.getLong(5), c.getString(6));
        }
        c.close();
        return null;
    }

    public int getChurchIdByCityAddress(String city, String address) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String selectquery = "SELECT * FROM " + DbConstants.Churches.DATABASE_TABLE + " WHERE " + DbConstants.Churches.CITY + "=\"" + city + "\" AND " + DbConstants.Churches.ADDRESS + "=\"" + address + "\"";
        Cursor c = mdb.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public void deleteAllDataFromDb() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        mdb.execSQL(DbConstants.Churches.DATABASE_DROP);
        mdb.execSQL(DbConstants.Weeks.DATABASE_DROP);
        mdb.execSQL(DbConstants.Worships.DATABASE_DROP);
        mdb.execSQL(DbConstants.Churches.DATABASE_CREATE);
        mdb.execSQL(DbConstants.Weeks.DATABASE_CREATE);
        mdb.execSQL(DbConstants.Worships.DATABASE_CREATE);
    }

    public void setUpdatedTime() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        mdb.delete(DbConstants.UpdatedTime.DATABASE_TABLE, DbConstants.UpdatedTime.ID + "=1", null);
        ContentValues values = new ContentValues();
        values.put(DbConstants.UpdatedTime.ID, 1);
        Date date = new Date(System.currentTimeMillis());
        TimeZone timeZone = TimeZone.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(date);
        //Log.d("sh", "set udpated time: " + currentDateandTime);
        values.put(DbConstants.UpdatedTime.LAST_MODIFIED, currentDateandTime);
        mdb.insert(DbConstants.UpdatedTime.DATABASE_TABLE, null, values);

    }

    public String getUpdatedTime() {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String selecquery = "SELECT * FROM " + DbConstants.UpdatedTime.DATABASE_TABLE + " WHERE " + DbConstants.UpdatedTime.ID + "=1";
        Cursor c = mdb.rawQuery(selecquery, null);
        if (c.moveToFirst()) {
            //Log.d("sh", "get updated time: " + c.getString(1));
            return c.getString(1);
        }

        return "hiba";
    }

    public void deleteWorshipById(int worshipid) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        mdb.delete(DbConstants.Worships.DATABASE_TABLE, DbConstants.Worships.WORSHIPID + "=" + worshipid, null);
    }

    public void modifyWorshipById(Worship worship) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(DbConstants.Worships.CHURCHID, worship.getChurchid());
        values.put(DbConstants.Worships.TERMIN, worship.getTermin());
        values.put(DbConstants.Worships.COMMENT, worship.getComment());
        values.put(DbConstants.Worships.WEEKID, worship.getWeekid());
        mdb.update(DbConstants.Worships.DATABASE_TABLE, values, DbConstants.Worships.WORSHIPID + "=" + worship.getWorshipid(), null);
    }


    public void modifyChurchById(Church church) {
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(DbConstants.Churches.CHURCHID, church.getChurchid());
        values.put(DbConstants.Churches.CONID, church.getConid());
        values.put(DbConstants.Churches.CITY, church.getCity());
        values.put(DbConstants.Churches.ADDRESS, church.getAddress());
        values.put(DbConstants.Churches.LAT, church.getLatitude());
        values.put(DbConstants.Churches.LON, church.getLongitude());
        values.put(DbConstants.Churches.COMMENT, church.getComment());
        mdb.update(DbConstants.Churches.DATABASE_TABLE, values, DbConstants.Churches.CHURCHID + "=" + church.getChurchid(), null);
    }

    public boolean isEmpty(){
        if(!mdb.isOpen()) {
            mdb = dbHelper.getReadableDatabase();
        }
        String count = "SELECT * FROM churches";
        Cursor c = mdb.rawQuery(count, null);
        int countchurches = c.getCount();
        count = "SELECT * FROM worships";
        c = mdb.rawQuery(count, null);
        int countworships = c.getCount();
        count = "SELECT * FROM weeks";
        c = mdb.rawQuery(count, null);
        int countweeks = c.getCount();
        Log.d("sh", "countchurches " + countchurches + "  countworships " + countworships + " countweeks " + countweeks);
        if((countchurches==0) || (countweeks==0) ||(countworships==0)){
            deleteAllDataFromDb();
            return true;
        } else {
            return false;
        }
    }
}
