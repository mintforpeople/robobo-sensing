package com.mytechia.robobo.framework.sensing.accel.android;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.sensing.accel.AAccelerationModule;

/*******************************************************************************
 * Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 * Copyright 2016 Luis Llamas <luis.llamas@mytechia.com>
 * <p>
 * This file is part of Robobo Sensing Modules.
 * <p>
 * Robobo Sensing Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Robobo Acceleration Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Sensing Modules.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class AndroidAccelerationModule extends AAccelerationModule implements SensorEventListener {

    private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms


    private String TAG = "AndroidAcceleration";

    private SensorManager mSensorManager;

    @Nullable
    private Sensor mAccelerationSensor;

    private int mLastAccuracy;

    private Context context;

    private int threshold = 20;

    private float lastx = 0;
    private float lasty = 0;
    private float lastz = 0;




    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        context = manager.getApplicationContext();

        try {
            rcmodule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            Log.d(TAG,"Remote module not available, not using it");
        }

        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);

        // Can be null if the sensor hardware is not available

        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void shutdown() throws InternalErrorException {

    }

    @Override
    public String getModuleInfo() {
        return null;
    }

    @Override
    public String getModuleVersion() {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            boolean notifychange = false;
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if (Math.abs(lastx-x)>threshold){
                notifychange = true;
            }
            if (Math.abs(lastx-x)>threshold){
                notifychange = true;
            }
            if (Math.abs(lastx-x)>threshold){
                notifychange = true;
            }
            if (notifychange){
                notifyAccelerationChange();
            }

            lastx = x;
            lasty = y;
            lastz = z;



            notifyAcceleration(Math.round(x),Math.round(y),Math.round(z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void setDetectionThreshold(int threshold) {
        this.threshold = threshold;
    }
}
