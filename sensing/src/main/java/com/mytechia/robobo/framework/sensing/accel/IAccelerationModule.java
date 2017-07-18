
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

import com.mytechia.robobo.framework.IModule;



/**
 * Interface of the ROBOBO acceleration module
 */
public interface IAccelerationModule extends IModule {

    /**
     * Suscribes a listener to the acceleration information stream
     * @param listener The listener to be suscribed
     */
    void suscribe(IAccelerationListener listener);

    /**
     * Unsuscribes a listener to the acceleration information stream
     * @param listener The listener to be suscribed
     */
    void unsuscribe(IAccelerationListener listener);

    /**
     * Sets the amount of change required to trigger a detection
     * @param threshold The amount of change required
     */
    void setDetectionThreshold(int threshold);

    /**
     * Starts a calibration
     * @param activate
     */
    void setCalibration(boolean activate);

}
