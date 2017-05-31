package com.worshipsearcher.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.worshipsearcher.R;
import com.worshipsearcher.services.ImageDownLoader;


public class ChurchImageActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.churcimage);
        Intent intent = getIntent();
        Integer churchid = intent.getIntExtra("churchid",0);
        ImageDownLoader imageDownLoader = new ImageDownLoader(this);
        Bitmap bmp = imageDownLoader.downoload(churchid);
        ImageView churchImageView = (ImageView) findViewById(R.id.churchImageView);
        if(bmp!=null) {
            churchImageView.setImageBitmap(bmp);
        } else {
            Drawable drawable = ContextCompat.getDrawable(this,R.drawable.nophoto);
            churchImageView.setImageDrawable(drawable);
        }
    }


    //File imageFile = new File(ImageDownLoader.getSdpath()+"/churches/"+0+".jpg");
        /*Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Integer churchid = bundle.getInt("churchid");
        Log.d("sh", ImageDownLoader.getSdpath());
        //File imageFile = new File(ImageDownLoader.getSdpath()+"/churches/"+churchid.toString()+".jpg");
       if(imageFile.exists()){
            Log.d("sh","exists");
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            churchImageView.setImageBitmap(bitmap);
        } else {
            imageFile = new File(ImageDownLoader.getSdpath()+"/churches/0.jpg");
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            churchImageView.setImageBitmap(bitmap);
        }*/

}
