package io.github.ohbalcony.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an irrigation Zone, i.e. a group of plants that are watered together.
 * 
 * @author Hermann Czedik-Eysenerg
 */
public class Zone {
    public List<HardwareReference> hardwareComponents = new ArrayList<>();
}
