package com.worshipsearcher.dbloader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mihaly on 2015.04.24..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, DbConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.Churches.DATABASE_CREATE);
        db.execSQL(DbConstants.Worships.DATABASE_CREATE);
        db.execSQL(DbConstants.Weeks.DATABASE_CREATE);
        db.execSQL(DbConstants.UpdatedTime.DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}