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
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.power.IPowerModeListener;
import com.mytechia.robobo.framework.power.PowerMode;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.sensing.accel.AAccelerationModule;


/**
 * Implementation of the Acceleration module
 */
public class AndroidAccelerationModule extends AAccelerationModule implements SensorEventListener, IPowerModeListener {

    private static final int SENSOR_DELAY_MICROS = 100 * 1000; // 50ms


    private String TAG = "AndroidAcceleration";

    private SensorManager mSensorManager;

    @Nullable
    private Sensor mAccelerationSensor;

    private int mLastAccuracy;

    private Context context;

    private double threshold = 0.08;

    private double lastx = 0;
    private double lasty = 0;
    private double lastz = 0;

    private boolean calibrating = false;




    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        m = manager;
        context = manager.getApplicationContext();

        m.subscribeToPowerModeChanges(this);

        try {
            rcmodule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            m.log(LogLvl.WARNING, TAG,"Remote module not available, not using it");
        }

        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);

        // Can be null if the sensor hardware is not available
        enableAccelerometer();

    }

    @Override
    public void shutdown() throws InternalErrorException {
        mSensorManager.unregisterListener(this);

    }


    /** Susbcribes the module to the acceleration events
     *
     */
    private void enableAccelerometer() {

        disableAccelerometer();

        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mAccelerationSensor, SENSOR_DELAY_MICROS);
    }

    /** Unsubscribes the module from the acceleration events
     *
     */
    private void disableAccelerometer() {
        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.unregisterListener(this);
    }


    @Override
    public String getModuleInfo() {
        return "Acceleration Module";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            boolean notifychange = false;
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if ((Math.abs(lastx)-x)>threshold){
                notifychange = true;
            }
            if ((Math.abs(lasty)-y)>threshold){
                notifychange = true;
            }
            if ((Math.abs(lastz)-z)>threshold){
                notifychange = true;
            }

            if (notifychange){
                notifyAcceleration(x, y, z);
                lastx = x;
                lasty = y;
                lastz = z;
            }


            if (calibrating){
                notifyCalibrationAngle(Math.toDegrees(Math.acos(y/Math.sqrt(x*x+y*y+z*z)))*Math.signum(z));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void setDetectionThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void setCalibration(boolean activate) {
        calibrating = activate;
    }


    /** Enable or disable the reception of acceleration events depending on the robot power mode
     *
     * @param newMode new power mode
     */
    @Override
    public void onPowerModeChange(PowerMode newMode) {

        if (newMode == PowerMode.LOWPOWER) {
            disableAccelerometer();
        }
        else {
            enableAccelerometer();
        }

    }
}
