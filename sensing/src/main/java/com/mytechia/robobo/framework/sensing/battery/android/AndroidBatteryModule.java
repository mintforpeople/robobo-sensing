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
 * along withRobobo Sensing Modules.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/


package com.mytechia.robobo.framework.sensing.battery.android;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.sensing.battery.ABatteryModule;
import com.mytechia.robobo.framework.sensing.battery.IBatteryModule;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Implementation of the Battery sensor module
 */
public class AndroidBatteryModule extends ABatteryModule {

    private Context context;
    private Timer batterytimer;
    private TimerTask battTimerTask;
    private static final int statusPeriod = 3000;

    @Override
    public void startup(RoboboManager manager){
        m = manager;
        context = manager.getApplicationContext();
        batterytimer = new Timer();
        battTimerTask = new checkBatteryLevel();
        batterytimer.schedule(battTimerTask,1000,statusPeriod);

        try {
            rcmodule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() throws InternalErrorException {
        batterytimer.cancel();
        batterytimer.purge();
    }

    @Override
    public String getModuleInfo() {
        return "Battery Module";
    }

    @Override
    public String getModuleVersion() {
        return "0.3.0";
    }

    @Override
    public float getBatteryLevel() {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

    @Override
    public void setRefreshInterval(int millis) {
        batterytimer = new Timer();
        battTimerTask = new checkBatteryLevel();
        batterytimer.schedule(battTimerTask,0,millis);
    }

    /**
     * Timertask to check the battery
     */
    private class checkBatteryLevel extends TimerTask{

        @Override
        public void run() {
            notifyBattery(Math.round(getBatteryLevel()));
        }
    }
}
