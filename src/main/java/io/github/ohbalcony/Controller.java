package io.github.ohbalcony;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final Store store;

    @Autowired
    public Controller(Store store) {
        this.store = store;
    }

    @RequestMapping("/test")
    public SensorData test() {
        SensorData sensorData = new SensorData();
        sensorData.moisture.put("sensor1", 0.2);
        sensorData.moisture.put("sensor2", 0.3);
        sensorData.tanks.put("tank", 50.0);
        sensorData.pumps.put("pump1", true);
        sensorData.pumps.put("pump2", false);
        sensorData.valves.put("valve1", true);
        sensorData.valves.put("valve2", false);
        return sensorData;
    }

    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public SensorData store(@RequestBody SensorData sensorData) {
        store.save(sensorData);
        return sensorData;
    }

}
