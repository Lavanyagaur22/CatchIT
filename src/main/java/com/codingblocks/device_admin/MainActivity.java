package com.codingblocks.device_admin;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private boolean mAlreadyStartedService = false;

    String emailfinal, emailOncreate;
    TextView textViewEmail, display_tv;
    TextView mMsgView, permssion_tv;
    View onetime;
    LinearLayout linearLayout, linearemail;
    AlertDialog customDialog, customDialog2, colorview, al1;
    TextView attemptstv;
    String attemptstv_alias;
    SharedPreferences sharedPreferences, sharedPreferences2, sharedPreferences_location, sharedPreferences_switchh, sharedPreferences_view;
    AlertDialog alertDialog;
    Switch switch1;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private static final int ADMIN_INTENT = 15;
    private static final String description = "You need to activate Device Administrator to use this application";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linear);
        onetime = findViewById(R.id.onetime);

        sharedPreferences_view = getSharedPreferences("view", MODE_PRIVATE);
        Boolean b1 = sharedPreferences_view.getBoolean("checked1", false);
        Boolean b2 = sharedPreferences_view.getBoolean("checked2", false);
        if (!b1 && !b2) {
            linearLayout.setVisibility(View.VISIBLE);
            onetime.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
            onetime.setVisibility(View.GONE);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    123);


        }
        final Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Allow Display over other apps ?")

                        .setMessage("Grant the Permission for allowing display over other apps .\n\nIt's important to allow the Camera to capture the image of the person who enters the incorrect password . \n\nYou can " +
                                "remove this permission by opening Settings -> Apps -> Draw Over other Apps (Toggle the button accordingly) .")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("TAG", "onClick positive : yeee");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 0);
                                    }
                                }

                                sharedPreferences_view = getSharedPreferences("view", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences_view.edit();
                                editor.putBoolean("checked1", true);
                                editor.putBoolean("checked2", true);
                                editor.apply();
                                linearLayout.setVisibility(View.GONE);
                                onetime.setVisibility(View.GONE);

                            }
                        })

                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("TAG", "onClick negative : nop");
                                linearLayout.setVisibility(View.VISIBLE);
                                onetime.setVisibility(View.VISIBLE);

                            }
                        })
                        .create();

                al1.show();
            }
        });

        switch1 = findViewById(R.id.switch1);
        sharedPreferences_switchh = getSharedPreferences("my_switch", MODE_PRIVATE);
        Boolean b = sharedPreferences_switchh.getBoolean("checked", false);

        switch1.setChecked(b);
        textViewEmail = findViewById(R.id.tvemail);
        permssion_tv = findViewById(R.id.permission_tv);
        permssion_tv.setOnClickListener(this);
        linearemail = findViewById(R.id.linear_email);
        attemptstv = findViewById(R.id.attempts);

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);

        sharedPreferences2 = getSharedPreferences("my preference2", MODE_PRIVATE);
        attemptstv_alias = sharedPreferences2.getString("attempts", "");
        attemptstv.setText(attemptstv_alias);

        sharedPreferences = getSharedPreferences("my preference", MODE_PRIVATE);
        emailOncreate = sharedPreferences.getString("email", "");
        if (!emailOncreate.isEmpty()) {
            textViewEmail.setVisibility(View.VISIBLE);
            Log.e("TAG", "onCreate:--&&&&&@@@@@@@@&--***---- " + emailOncreate);
            textViewEmail.setText(emailOncreate);
        }

        mMsgView = findViewById(R.id.msgView);


        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

                        if (latitude != null && longitude != null) {
                            mMsgView.setText("\n Latitude : " + latitude + "\n Longitude: " + longitude);
                            sharedPreferences_location = getSharedPreferences("preference_location", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences_location.edit();

//                            editor.putString("my_location", latitude + ">,<" + longitude);
                            editor.putString("my_location", latitude + "," + longitude);
//                            editor.putString("my_location1",latitude);
//                            editor.putString("my_location2",longitude);

                            editor.apply();
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );


        Log.e("TAG", "onCreate:--&&&&&&--***---- " + attemptstv_alias);

        switch1.setOnCheckedChangeListener(this);

        linearemail.setOnClickListener(this);
        attemptstv.setOnClickListener(this);


        Log.e("TAG", "onCreate:-------- ");

    }


    private ComponentName getActiveComponentName() { //gets the current active component

        ComponentName componentName = null;

        List<ComponentName> activeComponentList = mDevicePolicyManager.getActiveAdmins();

        Iterator<ComponentName> iterator = activeComponentList.iterator();

        while (iterator.hasNext()) {

            componentName = (ComponentName) iterator.next();
        }
        return componentName;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

        sharedPreferences_switchh = getSharedPreferences("my_switch", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences_switchh.edit();
        editor.putBoolean("checked", b);
        editor.apply();

        if (b) {

            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    // .setCancelable(false)   bahr click karne pe cancellable nahi hoga
                    .setMessage("This app uses the Device\nAdministrator permission to monitor screen unlock attempts." +
                            "\n\nPlease activate this permission on the following screen." +
                            "\n\nYou can remove this permission at any time by switching off the alert emails." +
                            "\n\nThe app will never wipe your phone's data despite the warning shown.")

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e("TAG", "onClick positive : yipee");

                            Log.e("TAG", "onClick: checked " + b);
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
                            startActivityForResult(intent, ADMIN_INTENT);


                        }
                    })

                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e("TAG", "onClick negative : nop");
                            switch1.setChecked(false);
                            mDevicePolicyManager.removeActiveAdmin(mComponentName);
                        }
                    })
                    .create();

            alertDialog.show();
//            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();


        } else if (b == false) {

            mDevicePolicyManager.removeActiveAdmin(mComponentName);
            Toast.makeText(MainActivity.this, "Device admin permission NOT granted !", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onCheckedChanged: " + b);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Successfully Registered As Admin . Now you can try unlocking your phone with wrong password !", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }

        Log.e("TAG", "getDeviceName: " + capitalize(manufacturer) + " " + model);
        return capitalize(manufacturer) + " " + model;

    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linear_email:
                final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null, true);

                customDialog = new AlertDialog.Builder(this)
                        .setTitle("Enter Your Email here ! ")
                        .setView(dialogView)

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                EditText emailEt = dialogView.findViewById(R.id.etEmail);
                                String email = emailEt.getText().toString();
                                emailfinal = email;
                                textViewEmail.setVisibility(View.VISIBLE);
                                textViewEmail.setText(email);

                                sharedPreferences = getSharedPreferences("my preference", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.apply();
                                Toast.makeText(MainActivity.this, "Registered email :- " + email, Toast.LENGTH_SHORT).show();

                            }
                        })
                        .create();
                customDialog.show();
                break;

            case R.id.attempts:

                final View dialogView2 = LayoutInflater.from(this).inflate(R.layout.mydialog, null, true);

                customDialog2 = new AlertDialog.Builder(this)
                        .setTitle(" Number Of Attempts here ")
                        .setView(dialogView2)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                EditText attemptsEt = dialogView2.findViewById(R.id.etAttempts);
                                String no_of_attempts = attemptsEt.getText().toString();
                                attemptstv.setText(no_of_attempts);


                                sharedPreferences2 = getSharedPreferences("my preference2", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putString("attempts", no_of_attempts);
                                editor.apply();
                                Toast.makeText(MainActivity.this, "No. of attempts chosen :- " + no_of_attempts, Toast.LENGTH_SHORT).show();

                            }
                        })
                        .create();
                customDialog2.show();
                break;

            case R.id.permission_tv:

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            1283);


                } else {
                    Toast.makeText(MainActivity.this, "All the permissions are already granted !", Toast.LENGTH_LONG).show();


                }

                break;


        }
    }


//    private void switchColor(boolean checked) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            switch1.getThumbDrawable().setColorFilter(checked ? Color.RED : Color.GREEN, PorterDuff.Mode.MULTIPLY);
//            switch1.getTrackDrawable().setColorFilter(!checked ? Color.BLUE : Color.WHITE, PorterDuff.Mode.MULTIPLY);
//        }
//    }

    public void shareText(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Your shearing message goes here";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.changeTheme:
                colorview = new AlertDialog.Builder(this)
                        .setTitle("Select the color ! ")
                        .setItems(new CharSequence[]{"Default", "PaperDark", "PaperSky", "PaperLight"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        themeUtils.changeToTheme(MainActivity.this, themeUtils.Default);
                                        break;

                                    case 1:
                                        themeUtils.changeToTheme(MainActivity.this, themeUtils.BLUE);
                                        break;

                                    case 2:
                                        themeUtils.changeToTheme(MainActivity.this, themeUtils.Grey);
                                        break;

                                    case 3:
                                        themeUtils.changeToTheme(MainActivity.this, themeUtils.Nobita);
                                        break;

                                }
                            }
                        })
                        .create();

                colorview.show();
                return true;

            case R.id.help:

                Uri uri = Uri.parse("https://lavanyagaur.wixsite.com/catchit");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;

            case R.id.pdf:

                Uri uri1 = Uri.parse("https://docs.google.com/document/d/1yfmsCS_PY2CnXiF9oTV-aqCE4Bfts9fql7n7GIJ7V5w/edit?usp=sharing");
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                startActivity(intent1);
                return true;


            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check out this awesome Android app . It takes a photo of anyone who tries to unlock your phone with the wrong code : \nlink here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        startStep1();
    }


    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title_alert_no_intenet);
        builder.setMessage(R.string.msg_alert_no_internet);

        String positiveText = getString(R.string.btn_label_refresh);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {

                            //Now make sure about location permission.
                            if (checkPermissions()) {

                                //Step 2: Start the Location Monitor Service
                                //Everything is there to start the service.
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService && mMsgView != null) {

            mMsgView.setText(R.string.msg_location_service_started);

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//
//                    android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                                    REQUEST_PERMISSIONS_REQUEST_CODE);
//                        }
//                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
//    private void showSnackbar(final int mainTextStringId, final int actionStringId,
//                              View.OnClickListener listener) {
//        Snackbar.make(
//                findViewById(android.R.id.content),
//                getString(mainTextStringId),
//                Snackbar.LENGTH_INDEFINITE)
//                .setAction(getString(actionStringId), listener).show();
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();

            } else {
                // Permission denied.

                // Notify the img_user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the img_user for permission (device policy or "Never ask
                // again" prompts). Therefore, a img_user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
//                showSnackbar(R.string.permission_denied_explanation,
//                        R.string.settings, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Build intent that displays the App settings screen.
//                                Intent intent = new Intent();
//                                intent.setAction(
//                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package",
//                                        BuildConfig.APPLICATION_ID, null);
//                                intent.setData(uri);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        });
            }
        }
    }


    @Override
    public void onDestroy() {


        //Stop location sharing service to app server.........

        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................


        super.onDestroy();
    }


    public void onClickdelete(View view) {

        mDevicePolicyManager.removeActiveAdmin(mComponentName);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:com.codingblocks.device_admin"));
        startActivity(intent);

    }


}
