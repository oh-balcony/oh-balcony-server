package io.github.ohbalcony.model;

import java.util.HashMap;
import java.util.Map;

// TODO remove
public class SensorData {
    /**
     * Key: Name of soil moisture sensor.
     * Value: Sensor measurement (from 0.0 to 1.0)
     */
    public Map<String, Double> moisture = new HashMap<>();
    
    /**
     * Key: Name of Tank.
     * Value: Water level in tank (0.0 to 100.0)
     */
    public Map<String,Double> tanks = new HashMap<>();
    
    /**
     * Key: Name of Pump.
     * Value: If pump is on, true. If pump is off, false.
     */
    public Map<String,Boolean> pumps = new HashMap<>();
    
    /**
     * Key: Name of Valve.
     * Value: If Valve is open, true. If Valve is closed, false.
     */
    public Map<String,Boolean> valves = new HashMap<>();
}
