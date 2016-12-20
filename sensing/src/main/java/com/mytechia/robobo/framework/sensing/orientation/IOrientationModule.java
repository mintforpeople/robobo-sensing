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
 * Robobo Orientation Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Sensing Modules.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.mytechia.robobo.framework.sensing.orientation;

import com.mytechia.robobo.framework.IModule;



/**
 * Interface of the orientationn sensing module
 */
public interface IOrientationModule extends IModule {

    /**
     * Suscribes a listener to the orientation notifications
     * @param listener The listener to be added
     */
    void suscribe(IOrientationListener listener);

    /**
     * Unsuscribes a listener from the orientation notifications
     * @param listener The listener to be removed
     */
    void unsuscribe(IOrientationListener listener);



}
