package io.github.ohbalcony.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Complete state of the system for a specific user.
 * 
 * @author Hermann Czedik-Eysenberg
 */
public class SystemState {
    public List<HardwareController> controllers = new ArrayList<>();
    
    public List<Zone> zones = new ArrayList<>();
    
    public Optional<Hardware> getHardwareByReference(HardwareReference hardwareReference) {
        return getControllerById(hardwareReference.controllerId).flatMap(c -> c.getHardwareById(hardwareReference.componentId));
    }

    public Optional<HardwareController> getControllerById(String controllerId) {
        return controllers.stream()
                .filter(c -> c.id.equals(controllerId))
                .findFirst();
    }
}
