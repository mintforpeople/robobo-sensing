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

package com.mytechia.robobo.framework.sensing.brightness;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;
import com.mytechia.robobo.framework.sensing.ASensingModule;

import java.util.HashSet;


/**
 * Abstract class managing listeners and status posting
 */
public abstract class ABrightnessModule extends ASensingModule implements IBrightnessModule {

    private static final String AMBIENTLIGHT_STATUS = "AMBIENTLIGHT";
    private static final long MAX_REMOTE_NOTIFICATION_PERIOD = 500; //ms

    private HashSet<IBrightnessListener> listeners;

    protected IRemoteControlModule rcmodule = null;
    protected RoboboManager m;

    public ABrightnessModule(){
        super(MAX_REMOTE_NOTIFICATION_PERIOD);
        listeners = new HashSet<IBrightnessListener>();
    }

    @Override
    public void suscribe(IBrightnessListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsuscribe(IBrightnessListener listener) {
        if (listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    /** Notifies all listeners of a change in brightness
     *
     * @param value brightness value
     * @param forceNotification forces the notificaion even at higher rate than the limit
     */
    protected void notifyBrightness(float value, boolean forceNotification){
        for (IBrightnessListener listener:listeners){
            listener.onBrightness(value);
        }

        if (rcmodule!=null && (canNotify() || forceNotification)) {
            Status s = new Status(AMBIENTLIGHT_STATUS);
            s.putContents("level",String.valueOf(Math.round(value)));
            rcmodule.postStatus(s);
            updateLastNotificationTime();
        }
    }

    /**
     * Used to notify the listeners of a substantial change on the sensor
     */
    @Deprecated
    protected void notifyBrightnessChange(){
        for (IBrightnessListener listener:listeners){
            listener.onBrightnessChanged();
        }

        if (rcmodule!=null && canNotify()){
            Status s = new Status("BRIGHTNESSCHANGED");
            rcmodule.postStatus(s);
        }
    }
}


