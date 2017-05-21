package io.github.ohbalcony.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an irrigation Zone, i.e. a group of plants that are watered together.
 * 
 * @author Hermann Czedik-Eysenerg
 */
public class Zone {
    public String name;

    public List<HardwareReference> hardwareReferences = new ArrayList<>();

    public String activeCondition;
}
