package com.mytechia.robobo.framework.sensing.brightness;

import com.mytechia.robobo.framework.IModule;


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

public interface IBrightnessModule extends IModule {

    /**
     * Sets the time on milliseconds to send the brightness values
     * @param millis The time on milliseconds
     */
    void setRefreshRate(int millis);

    /**
     * Reads the value from the brightness sensor
     * @return The current sensor value
     */
    float readBrightnessValue();

    /**
     * Suscribes to the feed of sensor data
     * @param listener The listener to be notified
     */
    void suscribe(IBrightnessListener listener);

    /**
     * Sets the percentage of change to be notified for a change
     * @param amount The amount of change to be notified
     */
    void setHasChangedAmount(int amount);


    /**
     * Unuscribes to the feed of sensor data
     * @param listener The listener to be unsuscribed
     */
    void unsuscribe(IBrightnessListener listener);
}
