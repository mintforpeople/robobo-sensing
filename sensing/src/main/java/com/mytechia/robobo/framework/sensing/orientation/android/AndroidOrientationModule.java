
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
 * Robobo Sensing Modules Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Sensing Modules.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/


package com.mytechia.robobo.framework.sensing.orientation.android;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.power.IPowerModeListener;
import com.mytechia.robobo.framework.power.PowerMode;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.sensing.orientation.AOrientationModule;
import com.mytechia.robobo.framework.sensing.orientation.IOrientationListener;

/**
 * Implementation of the ROBOBO orientation sensing module for android.
 */
public class AndroidOrientationModule extends AOrientationModule implements SensorEventListener, IPowerModeListener {

    private static final int SENSOR_DELAY_MICROS = 200 * 1000; // 250ms, 5 per second


    private String TAG = "AndroidOrientation";

    private SensorManager mSensorManager;

    @Nullable
    private Sensor mRotationSensor;

    private int mLastAccuracy;

    private int lastyaw =0;
    private int lastroll = 0;
    private int lastpitch = 0;

    private Context context;


    @Override
    public void startup(RoboboManager manager){
        m = manager;

        m.subscribeToPowerModeChanges(this);

        context = manager.getApplicationContext();

        try {
            remoteControlModule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            m.log(LogLvl.WARNING, TAG,"Remote module not available, not using it");
        }

        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);

        enableOrientation();
    }

    @Override
    public void shutdown() throws InternalErrorException {
        mSensorManager.unregisterListener(this);
    }


    /** Enables the sensor by registening a listener of TYPE_ROTATION_VECTOR events.
     */
    private void enableOrientation() {

        disableOrientation();

        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (mRotationSensor != null) {
            mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY_MICROS);
        }

    }

    /** Disables the sensor by removing the listener
     */
    private void disableOrientation() {
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onPowerModeChange(PowerMode newMode) {
        if (newMode == PowerMode.LOWPOWER) {
            disableOrientation();
        }
        else {
            enableOrientation();
        }
    }

    @Override
    public String getModuleInfo() {
        return "Orientation Module";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (mLastAccuracy != accuracy) {
            mLastAccuracy = accuracy;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        if (event.sensor == mRotationSensor) {
            updateOrientation(event.values);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void updateOrientation(float[] rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        worldAxisForDeviceAxisX = SensorManager.AXIS_X;
        worldAxisForDeviceAxisY = SensorManager.AXIS_Z;


        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix);

        // Transform rotation matrix into azimuth/pitch/roll
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // Convert radians to degrees
        float yaw = orientation[0] * -57;
        float pitch = orientation[1] * -57;
        float roll = orientation[2] * -57;


        //Log.d(TAG,rotationVector.toString());

        if ((Math.round(yaw) != lastyaw)||(Math.round(pitch) != lastpitch)||(Math.round(roll) != lastroll)) {
            notifyOrientationChange(yaw, pitch, roll);
            lastpitch = Math.round(pitch);
            lastroll = Math.round(roll);
            lastyaw = Math.round(yaw);
        }
    }
}
