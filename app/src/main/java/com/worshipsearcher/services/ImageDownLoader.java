package com.worshipsearcher.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.Lock;


/**
 * Created by Mihaly on 2015.11.07..
 */
public class ImageDownLoader {

    private static String URLPath = "http://worshipsearcher.szalaimihaly.hu/churchimages/";
    private Bitmap bitmap;
    private boolean finished;
    private Context context;

    public ImageDownLoader(Context context){
        this.context=context;
        bitmap=null;
        finished=false;
    }

    public Bitmap downoload(final Integer churchid) {
        final Object object = new Object();
        final Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(URLPath+churchid+".jpg");
                    Log.d("sh",url.toString());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());;
                    finished=true;
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (FileNotFoundException e){
                    Log.d("sh","A fájl nem található");
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    finished = true;
                }
            }
        };
        thread.start();
        while (!finished) {
            Log.d("sh","waiting");
        }

        return bitmap;
    }


}
