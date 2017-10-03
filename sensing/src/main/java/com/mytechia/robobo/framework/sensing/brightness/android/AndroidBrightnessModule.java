
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
 * Robobo Brightness Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Sensing Modules.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.mytechia.robobo.framework.sensing.brightness.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.power.IPowerModeListener;
import com.mytechia.robobo.framework.power.PowerMode;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.sensing.brightness.ABrightnessModule;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;



/**
 * Implementation of the brightness sensing module
 */
public class AndroidBrightnessModule extends ABrightnessModule implements IPowerModeListener {

    private Context context;

    private String TAG = "AndroidBrightness";

    private float brightnessValue = 0;

    private float lastBrightnessValue = 0;

    private int changedValue = 100;

    private BrightnessSensorEventLister sensorListener;


    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        m = manager;

        m.subscribeToPowerModeChanges(this);

        context = manager.getApplicationContext();
        SensorManager sensorManager
                = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        try {
            rcmodule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }

        if (lightSensor == null){
            m.log(LogLvl.ERROR, TAG,"Brightness sensor not present on this device");
        }else {

            float max =  lightSensor.getMaximumRange();

            enableSensor();

        }


    }

    private void enableSensor() {

        if(this.sensorListener != null) {
            disableSensor();
        }

        SensorManager sensorManager
                = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.sensorListener = new BrightnessSensorEventLister();
        sensorManager.registerListener(
                this.sensorListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }


    private void disableSensor() {
        SensorManager sensorManager
                = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this.sensorListener);
    }


    @Override
    public void onPowerModeChange(PowerMode newMode) {
        if (newMode == PowerMode.LOWPOWER) {
            disableSensor();
        }
        else {
            enableSensor();
        }
    }


    @Override
    public void shutdown() throws InternalErrorException {

    }

    @Override
    public String getModuleInfo() {
        return "Brightness Module";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }



    @Override
    public float readBrightnessValue() {
        return 0;
    }

    @Override
    public void setHasChangedAmount(int amount) {
        changedValue = amount;
    }


    private class BrightnessSensorEventLister implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            lastBrightnessValue = brightnessValue;
            brightnessValue = event.values[0];

            if (Math.abs(lastBrightnessValue-brightnessValue) > changedValue){
                notifyBrightnessChange();
                //if changes are small we only notified at the limited rate, if changes are big, we force the notification
                notifyBrightness(brightnessValue, true);
            }

            notifyBrightness(brightnessValue, false);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }


}
