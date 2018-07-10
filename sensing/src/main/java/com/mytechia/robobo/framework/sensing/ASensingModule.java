
/*******************************************************************************
 * Copyright 2017 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 * Copyright 2017 Gervasio Varela <gervasio.varela@mytechia.com>
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


package com.mytechia.robobo.framework.sensing;


/**
 * ASensingModule
 *
 *
 * Abstract class to add simple logic to every sensing module to control the frequency of remote value notifications
 * so that they can't saturate the network.
 *
 */

public abstract class ASensingModule {

    private long maxRemoteNotificationPeriod; //ms
    private long lastNotificationTime = 0;

    public ASensingModule(long maxRemoteNotificationPeriod) {
        this.maxRemoteNotificationPeriod = maxRemoteNotificationPeriod;
    }

    protected boolean canNotify() {
        return ((System.currentTimeMillis() - this.lastNotificationTime) > maxRemoteNotificationPeriod);
    }

    protected void updateLastNotificationTime() {
        this.lastNotificationTime = System.currentTimeMillis();
    }

    protected void setMaxRemoteNotificationPeriod(long millis){
        this.maxRemoteNotificationPeriod = millis;
    }

}
