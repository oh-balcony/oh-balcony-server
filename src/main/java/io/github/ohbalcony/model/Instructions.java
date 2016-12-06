package io.github.ohbalcony.model;

import java.util.HashMap;
import java.util.Map;

public class Instructions {
    /**
     * Key: Name of Pump.
     * Value: If pump should be turned on, true. If pump should be off, false.
     */
    public Map<String,Boolean> pumps = new HashMap<>();
    
    /**
     * Key: Name of Valve.
     * Value: If Valve should be opened, true. If Valve should be closed, false.
     */
    public Map<String,Boolean> valves = new HashMap<>();
}
