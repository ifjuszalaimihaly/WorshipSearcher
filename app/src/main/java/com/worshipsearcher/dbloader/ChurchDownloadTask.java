package com.worshipsearcher.dbloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worshipsearcher.entities.Church;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MihálySzalai on 2016. 08. 13..
 */
public class ChurchDownloadTask extends AsyncTask<Void, Void, ArrayList<Church>> {

    private Context context;

    private static final String URLPATH = "http://worshipsearcher.szalaimihaly.hu/";
    private ProgressDialog dialog = null;

    public ChurchDownloadTask(Context context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("töltés");
    }

    @Override
    protected ArrayList<Church> doInBackground(Void... voids) {
        ArrayList<Church> churches = new ArrayList<>();
        try {
            Log.d("sh", "getallchurches");
            URL url = new URL(URLPATH + "getallchurches.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "UTF-8");

            con.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    con.getOutputStream(), "UTF-8");
            outputStreamWriter.flush();
            outputStreamWriter.close();
            Integer responseCode = con.getResponseCode();
            Log.d("sh", "responsecode " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            Integer i = in.read();
            Log.d("sh",i.toString());
            Log.d("sh",in.readLine());
            Log.d("sh",in.ready() + " ");
            while (in.ready()) {
                String s = in.readLine();
                Log.d("sh", s);
                if (s != null) {
                    try {
                        Log.d("sh",s);
                        JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                        short churchid = jsonObject.get("churchid").getAsShort();
                        int conid = jsonObject.get("conid").getAsInt();
                        String city = jsonObject.get("city").getAsString().trim();
                        String address = jsonObject.get("address").getAsString().trim();
                        float latitude = jsonObject.get("lat").getAsFloat();
                        float longitude = jsonObject.get("lon").getAsFloat();
                        String comment = jsonObject.get("comment").toString();
                        comment = comment.substring(1,comment.length()-1);
                        Log.d("sh", "comment " + comment);
                        Church church = new Church(churchid, conid, city, address, latitude, longitude, comment);
                        churches.add(church);
                        Log.d("sh", church.toString());
                    } catch (SQLiteException e){
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return churches;
    }

    @Override
    protected void onPostExecute(ArrayList<Church> churches) {
        super.onPostExecute(churches);
        dialog.dismiss();
    }
}
