package io.github.ohbalcony.model;

import java.util.HashMap;
import java.util.Map;

public class SensorData {
    /**
     * Key: Name of soil moisture sensor.
     * Value: Sensor measurement (from 0.0 to 1.0)
     */
    public Map<String, Double> moisture = new HashMap<>();
    
    /**
     * Key: Name of temperature sensor.
     * Value: Sensor measurement (degree Celcius)
     */
    public Map<String, Double> temperature = new HashMap<>();
    
    
    /**
     * Key: Name of Tank.
     * Value: Water level in tank (0.0 to 100.0)
     */
    public Map<String,Double> tanks = new HashMap<>();
}
