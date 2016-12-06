package io.github.ohbalcony.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A piece of hardware controlling components of the system, i.e. a Raspberry Pi or Arduino.
 * 
 * @author Hermann Czedik-Eysenberg
 */
public class HardwareController extends Hardware {

    public List<Pump> pumps = new ArrayList<>();
    public List<Valve> valves = new ArrayList<>();
    public List<Tank> tanks = new ArrayList<>();
    public List<MoistureSensor> moistureSensors = new ArrayList<>();
    
    public Optional<Hardware> getHardwareById(String componentId) {
        return getAllComponents().filter(h -> h.id.equals(componentId)).findFirst();
    }

    private Stream<Hardware> getAllComponents() {
        return Stream.concat(Stream.concat(Stream.concat(pumps.stream(), valves.stream()), tanks.stream()), moistureSensors.stream());
    }
}
