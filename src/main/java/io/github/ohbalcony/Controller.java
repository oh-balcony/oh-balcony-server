package io.github.ohbalcony;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ohbalcony.model.ControllerState;
import io.github.ohbalcony.model.Hardware;
import io.github.ohbalcony.model.HardwareController;
import io.github.ohbalcony.model.HardwareReference;
import io.github.ohbalcony.model.Instructions;
import io.github.ohbalcony.model.Pump;
import io.github.ohbalcony.model.SensorData;
import io.github.ohbalcony.model.SensorValue;
import io.github.ohbalcony.model.SensorValues;
import io.github.ohbalcony.model.SystemState;
import io.github.ohbalcony.model.Valve;
import io.github.ohbalcony.model.Zone;
import io.swagger.annotations.ApiOperation;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    
    private final Store store;
    private final ControllerManager manager;

    private final SystemState systemState;

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private final JexlEngine jexl;

    @Autowired
    public Controller(Store store, ControllerManager manager) {
        this.store = store;
        this.manager = manager;

        jexl = new JexlBuilder().cache(100).namespaces(Collections.singletonMap(null, ExpressionFunctions.class))
                .create();

        try (InputStream is = this.getClass().getResourceAsStream("/state.json")) {
            systemState = jsonMapper.readValue(is, SystemState.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @ApiOperation("Gets the complete system state for the currently logged in user")
    @GetMapping(value = "/api/systemState")
    public SystemState getSystemState() {
        return systemState;
    }

    @ApiOperation("Gets sensor values for a given time range")
    @GetMapping(value = "/api/sensorValues")
    public SensorValues getSensorValues(@RequestParam long fromTimeMillis, @RequestParam long toTimeMillis) {

        // TODO
        log.info("from: {} to: {}", new Date(fromTimeMillis), new Date(toTimeMillis));

        SensorValues sensorValues = new SensorValues();
        sensorValues.values
                .add(new SensorValue(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(3)), 0.1));
        sensorValues.values
                .add(new SensorValue(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2)), 0.7));
        sensorValues.values.add(new SensorValue(new Date(System.currentTimeMillis()), 0.3));
        return sensorValues;
    }

    @RequestMapping(value = "/api/updateControllerState/{id}", method = RequestMethod.POST)
    public Instructions updateControllerState(@RequestBody SensorData sensorData,
            @PathVariable("id") String controllerId) {
        HardwareController controller = systemState.getControllerById(controllerId).get();
        
        Instructions instructions = new Instructions();
        
        // TODO we should actually make all the values from this zone available, not just this controller
        JexlContext expressionContext = new MapContext();
        Stream.concat(sensorData.moisture.entrySet().stream(),
                      Stream.concat(sensorData.tanks.entrySet().stream(),
                                    sensorData.temperature.entrySet().stream()))
            .forEach(e -> expressionContext.set(e.getKey(), e.getValue()));

        for (Zone zone : systemState.zones) {

            // TODO security: accepting just any condition without any sandboxing is dangerous
            JexlExpression expression = jexl.createExpression(zone.activeCondition);

            boolean shouldBeActive = (boolean) expression.evaluate(expressionContext);

            for (HardwareReference hwRef : zone.hardwareReferences) {
                if (!controller.id.equals(hwRef.controllerId))
                    continue;

                Optional<Hardware> hwOpt = controller.getHardwareById(hwRef.componentId);
                if (!hwOpt.isPresent()) {
                    log.error("Unknown component {} for controller {}", hwRef.componentId, hwRef.controllerId);
                    continue;
                }

                Hardware hw = hwOpt.get();
                Map<String, Boolean> toConfigure = null;
                if (hw instanceof Pump) {
                    toConfigure = instructions.pumps;
                } else if (hw instanceof Valve) {
                    toConfigure = instructions.valves;
                }
                if (toConfigure != null) {
                    Boolean currentValue = toConfigure.get(hw.id);
                    // note: active overwrites inactive from other zone
                    if (currentValue == null || !currentValue)
                        toConfigure.put(hw.id, shouldBeActive);
                }
            }
        }

        store.save(sensorData, instructions);
        
        return instructions;
    }

    // currently only used for Aquarium:

    @GetMapping(value = "/api/controller/{id}")
    public ControllerState getControllerState(@PathVariable("id") String controllerId) {
        return manager.getState(controllerId);
    }

    @RequestMapping(value = "/api/updateController", method = RequestMethod.POST)
    public void updateController(@RequestBody ControllerState state) {
        manager.updateState(state);
    }
}
