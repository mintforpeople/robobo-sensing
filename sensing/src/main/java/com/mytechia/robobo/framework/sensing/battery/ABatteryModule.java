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

package com.mytechia.robobo.framework.sensing.battery;

import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import java.util.HashSet;

import javax.crypto.spec.RC2ParameterSpec;


/**
 * Abstract class managing listeners and status posting
 */
public abstract class ABatteryModule implements IBatteryModule {
    protected HashSet<IBatteryListener> listeners = new HashSet<IBatteryListener>();
    protected IRemoteControlModule rcmodule = null;

    @Override
    public void suscribe(IBatteryListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsuscribe(IBatteryListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the battery level to the listeners
     * @param level Actual level of the battery
     */
    protected void notifyBattery(int level){
        for (IBatteryListener l : listeners){
            l.onNewBatteryStatus(level);
        }
        if (rcmodule!=null){
            Status s = new Status("OBOBATTLEV");
            s.putContents("level",String.valueOf(level));
            rcmodule.postStatus(s);
        }
    }
}
