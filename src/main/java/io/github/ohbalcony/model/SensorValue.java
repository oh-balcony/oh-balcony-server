package io.github.ohbalcony.model;

import java.util.Date;

public class SensorValue {
    /**
     * Date & Time of sensor reading.
     */
    public Date x;

    /**
     * Value.
     */
    public double y;

    public SensorValue() {
        // default
    }

    public SensorValue(Date datetime, double value) {
        this.x = datetime;
        this.y = value;
    }
}
