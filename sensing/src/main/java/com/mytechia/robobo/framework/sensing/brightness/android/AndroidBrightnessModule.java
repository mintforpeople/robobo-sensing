
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
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.sensing.brightness.ABrightnessModule;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;



/**
 * Implementation of the brightness sensing module
 */
public class AndroidBrightnessModule extends ABrightnessModule {

    private Context context;

    private String TAG = "AndroidBrightness";

    private float brightnessValue = 0;

    private float lastBrightnessValue = 0;

    private int changedValue = 20;

    private Timer brightnessTimer;

    private int refreshRate = 100;

    private BrightnessTask task;


    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
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
            Log.e(TAG,"Brightness sensor not present on this device");
        }else {
            float max =  lightSensor.getMaximumRange();


            sensorManager.registerListener(new SensorEventListener() {
                                               @Override
                                               public void onSensorChanged(SensorEvent event) {
                                                   lastBrightnessValue = brightnessValue;
                                                   brightnessValue = event.values[0];

                                                   if (((lastBrightnessValue/brightnessValue)> changedValue)||((brightnessValue/lastBrightnessValue)> changedValue)){
                                                       notifyBrightnessChange();
                                                   }
                                               }

                                               @Override
                                               public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                               }
                                           },
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

            brightnessTimer = new Timer();
            brightnessTimer.scheduleAtFixedRate(new BrightnessTask(),100,refreshRate);
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
        return "0.1";
    }





    @Override
    public void setRefreshRate(int millis) {
        refreshRate = millis;
        if (brightnessTimer!=null){
            task.cancel();
            task = new BrightnessTask();
            brightnessTimer.scheduleAtFixedRate(task, 100,refreshRate);
            brightnessTimer.purge();

        }
    }

    @Override
    public float readBrightnessValue() {
        return 0;
    }

    @Override
    public void setHasChangedAmount(int amount) {
        changedValue = amount;
    }

    private class BrightnessTask extends TimerTask{

        @Override
        public void run() {
            //Log.d(TAG,"Notify Brightness");
            notifyBrightness(brightnessValue);
        }
    }
}
