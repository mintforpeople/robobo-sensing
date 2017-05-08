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
 * Robobo Sensing Modules are distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Sensing Modules.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.mytechia.robobo.framework.sensing.orientation;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import java.util.HashSet;


/**
 * Abstract class managing listeners and status posting
 */
public abstract class AOrientationModule implements IOrientationModule {
    private HashSet<IOrientationListener> listeners = new HashSet<IOrientationListener>();
    protected IRemoteControlModule remoteControlModule = null;
    protected RoboboManager m;
    @Override
    public void suscribe(IOrientationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsuscribe(IOrientationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies a change on the orientation
     * @param yaw Orientation at yaw axis
     * @param pitch Orientation at pitch axis
     * @param roll Orientation at roll axis
     */
    protected void notifyOrientationChange(float yaw, float pitch, float roll){
        for (IOrientationListener listener : listeners){
            listener.onOrientationChanged(yaw, pitch, roll);
        }

        if (remoteControlModule != null){

            Status status = new Status("ORIENTATION");
            status.putContents("yaw",String.valueOf(yaw));
            status.putContents("pitch",String.valueOf(pitch));
            status.putContents("roll",String.valueOf(roll));

            remoteControlModule.postStatus(status);
        }

    };


}
