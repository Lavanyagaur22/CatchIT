package com.codingblocks.device_admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.work.Worker;

import static android.content.Context.MODE_PRIVATE;

public class MyWorker extends Worker {

    String my_location1;
    String my_location;
    String my_location2;


    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences("my preference", MODE_PRIVATE);
        String email1 = sharedPreferences.getString("email", "");

        SharedPreferences sharedPreferences_location = context.getSharedPreferences("preference_location", MODE_PRIVATE);

//        my_location1 = sharedPreferences_location.getString("my_location1", "");
//        my_location2 = sharedPreferences_location.getString("my_location2", "");
        my_location = sharedPreferences_location.getString("my_location", "");



        try {

            File file = get_image_file();

            Log.e("TAG", "doWork: file" + file);
//            Bitmap bitmap = getBitmapImage();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] byteArray = stream.toByteArray();

//            FileOutputStream out = new FileOutputStream(file);
//
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

//            Uri uri=getImageUri(getApplicationContext(),bitmap);

//            Intent emailIntent = new Intent(Intent.ACTION_SEND);
//            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "lavanyagaur@gmail.com"});
//            emailIntent.setType("image/*");
//            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello...");
//            // + "\n\r" + "\n\r" +
//            // feed.get(Selectedposition).DETAIL_OBJECT.IMG_URL
//            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your tsxt here");
//            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//            getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//            Log.e("TAG", "doWork: my intent for email------- " );


            Log.e("TAG", "doWork: ------------------- imageFile " + file);


            GMailSender sender = new GMailSender("lvngaur@gmail.com", "Android2018");


//            Log.e("TAG", "doWork:---location--------- " + my_location1 + " helll " + my_location2);
            Log.e("TAG", "doWork:---location--------- " + my_location);


            if (my_location.equals(",")) {
                sender.sendMail("CatchIT ALERT ",


                        "CatchIT detected someone trying to unlock your " + MainActivity.getDeviceName() + " phone on " + getDateToStr()
                                + "\n" +
                                " \nTo see the location of the place where your phone was tried to unlock using the incorrect password , \nClick on the following link : " +
                                "\n\nHere's the google map's link for better accuracy (View Map)" +
//                            "\n\nhttp://maps.google.com/?q=<"+my_location + ">" + "\n" + bitmap, "lvngaur@gmail.com",
                                "https://www.google.com/maps/place/Coding+Blocks+:+Noida/@28.5852881,77.3105144,17z/data=!3m1!4b1!4m5!3m4!1s0x390ce53196c915cd:0x5e250f83bcb6ae0c!8m2!3d28.5852881!4d77.3127031" + "\n\nA photo has been attached below.\nPhotos are taken usin the front camera where possible  ", "lvngaur@gmail.com",


                        email1, file);


            } else {
                sender.sendMail("CatchIT ALERT ",


                        "CatchIT detected someone trying to unlock your " + MainActivity.getDeviceName() + " phone on " + getDateToStr()
                                + "\n" +
                                " \nTo see the location of the place where your phone was tried to unlock using the incorrect password , \nClick on the following link : " +
                                "\n\nHere's the google map's link for better accuracy (View Map)" +
//                            "\n\nhttp://maps.google.com/?q=<"+my_location + ">" + "\n" + bitmap, "lvngaur@gmail.com",
                                "\n\nhttp://maps.google.com/maps?q=" + my_location + ">" + "\n\nA photo has been attached below.\nPhotos are taken using the front camera where possible  ", "lvngaur@gmail.com",


                        email1, file);


            }

//            sender.sendMail("CatchIT ALERT ",
//
//
//                    "CatchIT detected someone trying to unlock your " + MainActivity.getDeviceName() + " phone on " + getDateToStr()
//                            + "\n" +
//                            " \nTo see the location of the place where your phone was tried to unlock using the incorrect password , \nClick on the following link : " +
//                            "\n\nHere's the google map's link for better accuracy (View Map)" +
////                            "\n\nhttp://maps.google.com/?q=<"+my_location + ">" + "\n" + bitmap, "lvngaur@gmail.com",
//                            "\n\nhttp://maps.google.com/maps?q="+my_location + ">" + "\n\nA photo has been attached below.\nPhotos are taken usin the front camera where possible  ", "lvngaur@gmail.com",
//
//
//                    email1,file);
//

//            sender.sendMail("Device_Admin ALERT ",
//
//
//                    String.valueOf(bitmap), "lvngaur@gmail.com",
//
//
//                    email1);

            Log.e("TAG", "doInBackground----------: " + MainActivity.getDeviceName());

            Log.e("TAG", "doInBackground----------: " + email1);
//            Log.e("TAG", "doWork: lalallallalaalalalalala" + getLatLng());


            //---------------------------------------


//            Uri pngUri = Uri.fromFile(file);
//
//
//            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//
//            emailIntent.setType("image/png");
//
//            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "lavanyagaur@gmail.com"});
//
//            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "bhoot");
//
//            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "fvagrefar");
//
//            emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
//
//            emailIntent.setType("image/png");
//
//            Log.e("TAG", "doWork : fraud email  ---------------- " );
//
//         getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));


            return Result.SUCCESS;


        } catch (Exception e) {


            Log.e("error", e.getMessage(), e);

            return Result.FAILURE;


        }
    }

    public String getDateToStr() {
        Date today = new Date();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today);


        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(today);

        Calendar calendar = Calendar.getInstance();
        ;
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        String timeme = time.format(calendar.getTime());


        return dayOfTheWeek + " , " + date + " at " + timeme + ".";

    }


//    public String getLatLng() {
//        final String[] myLatLng = new String[1];
////        final String myLatLng="";
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
//                new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
//                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
//
//                        if (latitude != null && longitude != null) {
////                            mMsgView.setText(  "\n Latitude : " + latitude + "\n Longitude: " + longitude);
//                            Log.e("TAG", "onReceive: latlong-----------" + latitude + longitude);
//                            myLatLng[0] += " Latitude is : " + latitude + " \n Longitude is : " + longitude;
////                            myLatLng=latitude+longitude;
//                        }
//                    }
//                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
//        );
//
//        return myLatLng[0];
//
//    }

//    public void startSER(){
//        Intent intent = new Intent(getApplicationContext(), LocationMonitoringService.class);
//        getApplicationContext().startService(intent);
//    }

    public File get_image_file() {

        File imageFile = null;
        Log.e("TAG", "onClick1: ----------------------__--__-");
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        // Put it in the image view
        if (cursor.moveToFirst()) {

            String imageLocation = cursor.getString(1);
            imageFile = new File(imageLocation);

        }
        return imageFile;

    }

    public Bitmap getBitmapImage() {

        Bitmap bm = null;
        Log.e("TAG", "onClick1: ----------------------__--__-");
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {   // TODO: is there a better way to do this?
                bm = BitmapFactory.decodeFile(imageLocation);
            }
        }
        return bm;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        Log.e("TAG", "getImageUri: Its an order" );
//        return Uri.parse(path);

        Uri imageUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName()
                + "/drawable/" + "ic_launcher");

        return imageUri;
    }


}
