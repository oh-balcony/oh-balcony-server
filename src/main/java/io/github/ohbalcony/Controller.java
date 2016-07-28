package io.github.ohbalcony;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    
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
    public Instructions store(@RequestBody SensorData sensorData) {
        store.save(sensorData);
        
        boolean shouldWater = shouldWater();
        
        // TODO hardcoded pump and valve names
        Instructions instructions = new Instructions();
        instructions.pumps.put("pump1", shouldWater);
        instructions.valves.put("valve1", shouldWater);
        return instructions;
    }

    private boolean shouldWater() {
        boolean shouldWater = false;
        
        // TODO hardcoded time
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(8, 7);
        
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();
        if (nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
            // TODO hardcoded tank name
            double tankWaterLevel = store.getCurrentTankLevel("tank1");
            
            shouldWater = tankWaterLevel > 0.0;
            
            if(shouldWater)
                log.info("Watering now");
            else
                log.info("NOT watering, because the tank is empty.");
        }
        return shouldWater;
    }

}
