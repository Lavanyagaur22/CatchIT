package com.codingblocks.device_admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.HiddenCameraFragment;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;

import androidx.work.Worker;

public class MyWorker2 extends Worker {

    @NonNull
    @Override
    public Result doWork() {

        Intent intent = new Intent(getApplicationContext(), DemoCamService.class);
        getApplicationContext().startService(intent);

        Log.e("TAG", "doWork: ------------- MyWorker2 " );
        return Result.SUCCESS;
    }


}