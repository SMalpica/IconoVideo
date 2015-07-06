package com.example.android.iconosvideos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.indexer.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import wseemann.media.FFmpegMediaMetadataRetriever;


public class MainActivity extends Activity {

    @Override
    @TargetApi(16)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Log.e("SYSO",encontrarDirectorio().getPath());
        String[] nombres = encontrarDirectorio().list();
        for(int i=0; i<nombres.length; i++){
            Log.e("SYSO",nombres[i]);
        }
        File archivo = new File(encontrarDirectorio().list()[0]);*/
//        String path = "/storage/emulated/0/Movies/videos360/video_1.mp4";
//        path = encontrarDirectorio().getAbsolutePath().toString();
//        Uri uri = Uri.parse(path);
//        System.out.println("path de la uri " + uri.getPath());
        File file = new File(encontrarDirectorio(),"video_2.mp4");
        FFmpegMediaMetadataRetriever md = new FFmpegMediaMetadataRetriever();
//        MediaMetadataRetriever md = new MediaMetadataRetriever();
        Log.e("SYSO", "existe: " + file.exists());
        Log.e("SYSO", "nombre: " + file.getAbsolutePath());
//        md.setDataSource(getApplicationContext(),uri);
        Bitmap bmp = null;
        try{
            md.setDataSource(file.getAbsolutePath());
//        Log.e("SYSO", "titulo del manager: "+md.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE));
//        String duracion = md
//                .extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
//        Log.e("SYSO", "duracion video: " + duracion);
//        md.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST);
            bmp = md.getFrameAtTime(5000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//        FFmpegFrameGrabber g = new FFmpegFrameGrabber("textures/video/anim.mp4");
//        Log.e("SYSO", "nombre bmp: "+bmp.toString());
//        g.start();
//        byte[] bytes =md.getEmbeddedPicture();
//        Log.e("SYSO", "bytes: "+bytes.length);
            md.release();

        /*try{
            FrameGrabber fg = FrameGrabber.createDefault(file);
            fg.start();
            // CanvasFrame, FrameGrabber, and FrameRecorder use Frame objects to communicate image data.
            // We need a FrameConverter to interface with other APIs (Android, Java 2D, or OpenCV).
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            // FAQ about IplImage and Mat objects from OpenCV:
            // - For custom raw processing of data, createBuffer() returns an NIO direct
            //   buffer wrapped around the memory pointed by imageData, and under Android we can
            //   also use that Buffer with Bitmap.copyPixelsFromBuffer() and copyPixelsToBuffer().
            // - To get a BufferedImage from an IplImage, or vice versa, we can chain calls to
            //   Java2DFrameConverter and OpenCVFrameConverter, one after the other.
            // - Java2DFrameConverter also has static copy() methods that we can use to transfer
            //   data more directly between BufferedImage and IplImage or Mat via Frame objects.
            IplImage grabbedImage = converter.convert(fg.grab());
            int width  = grabbedImage.width();
            int height = grabbedImage.height();
            IplImage grayImage    = IplImage.create(width, height, IPL_DEPTH_8U, 1);
            IplImage rotatedImage = grabbedImage.clone();
        }catch(org.bytedeco.javacv.FrameGrabber.Exception ex){
            Log.e("SYSO","error al crear el framegrabber");
        }*/

        /*MediaExtractor me = new MediaExtractor();
        try{
            me.setDataSource(file.getAbsolutePath());
            Log.e("SYSO","sampletime: "+me.getSampleTime());
        }catch(IOException ex){
            Log.e("SYSO","ha habido una excepcion IO");
        }*/


        }catch(IllegalArgumentException ex){Log.e("METADATA","illegal argument");}
        if(bmp == null){
            TextView texto = new TextView(getApplicationContext());
            texto.setText(file.getName());
            RelativeLayout padre = (RelativeLayout)findViewById(R.id.padre);
            padre.addView(texto);
        }else{
            ImageView view = (ImageView) findViewById(R.id.imageView);
            BitmapDrawable drawable = new BitmapDrawable(getResources(),bmp);
            view.setBackground(drawable);
        }
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

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * returns the directory where the app is going to store videos
     * @return
     */
    public static File encontrarDirectorio(){
        File directorio;
        //obtain the external or internal available directory
        if(isExternalStorageWritable()){
            Log.e("SYSO","almacenamiento externo se puede escribir");
            directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        }else{
            Log.e("SYSO","no se puede escribir");
            directorio = Environment.getDataDirectory();
        }
        File f = new File(directorio.getPath()+"/videos360/");
        if(!f.exists()){
            f.mkdirs();
        }
        return f;
    }
}
