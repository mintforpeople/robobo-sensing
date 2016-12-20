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

package com.mytechia.robobo.framework.sensing.accel;

import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import java.util.HashSet;



/**
 * Abstract class managing listeners and status posting
 */
public abstract class AAccelerationModule implements IAccelerationModule {
    private HashSet<IAccelerationListener> listeners = new HashSet<IAccelerationListener>();
    protected IRemoteControlModule rcmodule = null;

    @Override
    public void suscribe(IAccelerationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsuscribe(IAccelerationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies a notable change on the acceleration
     */
    protected void notifyAccelerationChange(){
        for (IAccelerationListener listener : listeners){
            listener.onAccelerationChange();
        }
        if (rcmodule!=null){
            Status status = new Status("ACCELCHANGE");
            rcmodule.postStatus(status);
        }
    }

    /**
     * Streams the acceleration data of the sensor
     * @param xaccel Acceleration on the x axis
     * @param yaccel Acceleration on the y axis
     * @param zaccel Acceleration on the z axis
     */
    protected void notifyAcceleration(int xaccel, int yaccel, int zaccel){
        for (IAccelerationListener listener : listeners){
            listener.onAcceleration(xaccel, yaccel,zaccel);
        }
        if (rcmodule!=null){
            Status status = new Status("ACCELERATION");
            status.putContents("xaccel",xaccel+"");
            status.putContents("yaccel",yaccel+"");
            status.putContents("zaccel",zaccel+"");
            rcmodule.postStatus(status);
        }
    }
}