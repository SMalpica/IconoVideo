package com.example.android.iconosvideos;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity {

    @Override
    @TargetApi(16)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = "android.resource://"+getPackageName()+"/raw/"+R.raw.pyrex;
        Uri uri = Uri.parse(path);
        System.out.println("path de la uri " + uri.getPath());
        MediaMetadataRetriever md = new MediaMetadataRetriever();
        md.setDataSource(getApplicationContext(),uri);
        md.getFrameAtTime(2000);
        Bitmap bmp = md.getFrameAtTime();
        ImageView view = (ImageView) findViewById(R.id.imageView);
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bmp);
        view.setBackground(drawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
