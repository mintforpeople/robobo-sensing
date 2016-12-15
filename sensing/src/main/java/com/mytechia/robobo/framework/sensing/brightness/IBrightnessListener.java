package com.mytechia.robobo.framework.sensing.brightness;

/**
 * Created by luis on 13/12/16.
 */

public interface IBrightnessListener {

    /**
     * Notifies a new brightness value
     * @param value
     */
    void onBrightness(float value);

    /**
     * Notifies when the brightness change a predefined amount
     */
    void onBrightnessChanged();

}
