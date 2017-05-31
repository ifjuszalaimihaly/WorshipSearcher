package com.worshipsearcher.dbloader;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


import com.loopj.android.http.RequestParams;
import com.worshipsearcher.entities.Church;
import com.worshipsearcher.entities.Week;
import com.worshipsearcher.entities.Worship;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;


import cz.msebera.android.httpclient.Header;


/**
 * Created by Mihaly on 2015.10.03..
 */
public class WorshipSearcher extends MultiDexApplication {


    private static DbLoader dbl;
    private static final String URLPATH = "http://worshipsearcher.szalaimihaly.hu/";
    public static boolean finished = false;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("create", "app oncreate");
        dbl = new DbLoader(getApplicationContext());
        dbl.open();
        //dbl.deleteAllDataFromDb();
        //Log.d("sh", "udated ;" + dbl.getUpdatedTime() + ";");
        //Log.d("sh", "létezik " + dbl.isChurchExits(501));
        //Log.d("sh", "létezik " + dbl.isChurchExits(601));
        if (isNetworkAvailable()) {
            try {
                //Log.d("sh", "Van adat: " + isdDataInDb());
                if (dbl.isEmpty()) {
                    Log.d("sh", "üres ");
                    dbl.deleteAllDataFromDb();
                    Log.d("sh", "nincs adat");
                    getAllChurches();
                    getAllWeeks();
                    getAllWorships();
                } else {
                    Log.d("sh", "van adat");
                    deleteOldWorships();
                    getNewChurches();
                    getNewWorships();
                }
                closeConnection();
                dbl.close();
                finished = true;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            if (dbl.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Nem sikerült az adatok letöltése. Ellenőrizze az internet kapcsolatot!", Toast.LENGTH_LONG).show();
            }
        }
        Log.d("sh", dbl.getUpdatedTime());

    }


    public static DbLoader getDbl() {
        return dbl;
    }

    public static boolean isdDataInDb() {
        boolean isDataInClient = true;
        if (dbl.getAllChurch().size() == 0) {
            isDataInClient = false;
        }
        if (dbl.getAllWeek().size() == 0) {
            isDataInClient = false;
        }
        if (dbl.getAllWroship().size() == 0) {
            isDataInClient = false;
        }
        /*if (!isDataInClient) {
            getDbl().deleteAllDataFromDb();
        }*/
        return isDataInClient;
    }


    public static void getAllChurches() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestHandle handle = asyncHttpClient.get(URLPATH + "getallchurches.php", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                BufferedReader in = new BufferedReader(inputStreamReader);
                try {
                    while (in.ready()) {
                        String s = in.readLine();
                        if (s != null && s.length() > 1) {
                            try {
                                Log.d("sh", s);
                                JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                                short churchid = jsonObject.get("churchid").getAsShort();
                                int conid = jsonObject.get("conid").getAsInt();
                                String city = jsonObject.get("city").getAsString().trim();
                                String address = jsonObject.get("address").getAsString().trim();
                                float latitude = jsonObject.get("lat").getAsFloat();
                                float longitude = jsonObject.get("lon").getAsFloat();
                                String comment = jsonObject.get("comment").toString();
                                comment = comment.substring(1, comment.length() - 1);
                                Church church = new Church(churchid, conid, city, address, latitude, longitude, comment);
                                if(!dbl.isChurchExits(churchid)) {
                                    dbl.createChurch(church);
                                } else {
                                    dbl.modifyChurchById(church);
                                }
                            } catch (SQLiteException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                //  Log.d("sh", "hiba");
            }
        });
        handle.isFinished();

    }


    public static void getAllWorships() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestHandle handle = asyncHttpClient.get(URLPATH + "getallworships.php", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                        BufferedReader in = new BufferedReader(inputStreamReader);
                        try {
                            while (in.ready()) {
                                String s = in.readLine();
                                Log.d("sh",s);
                                if (s != null && s.length() > 1) {
                                    try {
                                        JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                                        int worshipid = jsonObject.get("worshipid").getAsInt();
                                        short churchid = jsonObject.get("churchid").getAsShort();
                                        String termin = jsonObject.get("termin").getAsString();
                                        short weekid = jsonObject.get("weekid").getAsShort();
                                        String comment = jsonObject.get("comment").getAsString();
                                        termin = termin.substring(0, termin.length() - 3);
                                        if (comment.equals("undefined")) {
                                            comment = "";
                                        }
                                        Worship worship = new Worship(worshipid, churchid, termin, weekid, comment);
                                        if(!dbl.isWorshipExists(worshipid)) {
                                            dbl.createWorship(worship);
                                        } else {
                                            dbl.modifyWorshipById(worship);
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }
                }

        );
    }

    public static void getAllWeeks() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(URLPATH + "getallweeks.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                BufferedReader in = new BufferedReader(inputStreamReader);
                try {
                    while (in.ready()) {
                        String s = in.readLine();
                        if (s != null && s.length() > 1) {
                            try {
                                JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                                short weekid = jsonObject.get("weekid").getAsShort();
                                String type = jsonObject.get("type").getAsString();
                                Week week = new Week(weekid, type);
                                dbl.createWeek(week);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


    }

    public static void closeConnection() {
        dbl.setUpdatedTime();
    }


    public static void deleteOldWorships() {
        final String deleted = dbl.getUpdatedTime();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams("deleted", deleted);
        asyncHttpClient.post(URLPATH + "deleteoldworships.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                BufferedReader in = new BufferedReader(inputStreamReader);
                try {
                    while (in.ready()) {
                        String s = in.readLine();
                        if (s != null && s.length() > 1) {
                            try {
                                JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                                int worshipid = jsonObject.get("worshipid").getAsInt();
                                if(dbl.isWorshipExists(worshipid)) {
                                    dbl.deleteWorshipById(worshipid);
                                }
                                Log.d("sh", "deleted worships " + s);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }


    public static void getNewWorships() {
        String updated = dbl.getUpdatedTime();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams("updated", updated);
        asyncHttpClient.post(URLPATH + "getnewworships.php", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                BufferedReader in = new BufferedReader(inputStreamReader);
                try {
                    while (in.ready()) {
                        String s = in.readLine();
                        if (s != null && s.length() > 1) {
                            try {
                                Log.d("sh", ";" + s + ";");
                                JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                                int worshipid = jsonObject.get("worshipid").getAsInt();
                                short churchid = jsonObject.get("churchid").getAsShort();
                                String termin = jsonObject.get("termin").getAsString();
                                short weekid = jsonObject.get("weekid").getAsShort();
                                String comment = jsonObject.get("comment").getAsString();
                                termin = termin.substring(0, termin.length() - 3);
                                if (comment.equals("undefined")) {
                                    comment = "";
                                }
                                Worship worship = new Worship(worshipid, churchid, termin, weekid, comment);
                                if (!dbl.isWorshipExists(worshipid)) {
                                    dbl.createWorship(worship);
                                } else {
                                    dbl.modifyWorshipById(worship);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }


    public static void getNewChurches() {
        String updated = dbl.getUpdatedTime();
        Log.d("sh", "updated " + dbl.getUpdatedTime());
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams("updated", updated);
        RequestHandle handle = asyncHttpClient.post(URLPATH + "getnewchurches.php", requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                BufferedReader in = new BufferedReader(inputStreamReader);
                try {

                    while (in.ready()) {
                        String s = in.readLine();
                        Log.d("sh", s);
                        if (s != null && s.length() > 1) {
                            try {
                                Log.d("sh", s);
                                JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                                short churchid = jsonObject.get("churchid").getAsShort();
                                int conid = jsonObject.get("conid").getAsInt();
                                String city = jsonObject.get("city").getAsString().trim();
                                String address = jsonObject.get("address").getAsString().trim();
                                float latitude = jsonObject.get("lat").getAsFloat();
                                float longitude = jsonObject.get("lon").getAsFloat();
                                String comment = jsonObject.get("comment").toString();
                                comment = comment.substring(1, comment.length() - 1);
                                Log.d("sh", "comment " + comment);
                                Church church = new Church(churchid, conid, city, address, latitude, longitude, comment);
                                Log.d("sh", church.toString());
                                if (!dbl.isChurchExits(churchid)){
                                    dbl.createChurch(church);
                                } else {
                                    dbl.modifyChurchById(church);
                                }

                            } catch (SQLiteException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                //Log.d("sh", "hiba");
            }
        });
    }

    public static String slice(String s, char separator) {
        int i = 0;
        while (s.charAt(i) != separator && i < s.length()) {
            i++;
        }
        s = s.substring(0, i--).trim();
        return s;

    }

    public static String slice2(String s, char separator) {
        int i = 0;
        while (s.charAt(i) != separator && i < s.length()) {
            i++;
        }
        i++;
        s = s.substring(i).trim();
        return s;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
