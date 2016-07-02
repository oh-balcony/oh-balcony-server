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
        sensorData.sensorData.put("sensor1", 0.2);
        sensorData.sensorData.put("sensor2", 0.3);
        return sensorData;
    }

    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public SensorData store(@RequestBody SensorData sensorData) {
        store.save(sensorData);
        return sensorData;
    }

}
