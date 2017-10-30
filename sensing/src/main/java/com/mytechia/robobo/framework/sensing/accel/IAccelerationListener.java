/*******************************************************************************
 * Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 * Copyright 2016 Luis Llamas <luis.llamas@mytechia.com>
 * <p>
 * This file is part of Robobo Remote Control Module.
 * <p>
 * Robobo Remote Control Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Robobo Remote Control Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Remote Control Module.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.mytechia.robobo.framework.sensing.accel;


public interface IAccelerationListener {
    /**
     * Triggered when a notable change in acceleration is detected
     */
    void onAccelerationChange();

    /**
     * Notifies periodically the measured information on the acceleration sensor
     * @param xaccel Acceleration on X axis
     * @param yaccel Acceleration on Y axis
     * @param zaccel Acceleration on Z axis
     */
    void onAcceleration(double xaccel, double yaccel, double zaccel);

    /**
     * Used for calibration
     * @param angle tilt angle
     */
    void onCalibrationAngle(double angle);

}
