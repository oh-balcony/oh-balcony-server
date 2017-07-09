package io.github.ohbalcony.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ControllerState {
    public String controllerId;

    public volatile Map<String, Double> values = new LinkedHashMap<>();

    @JsonIgnore
    public void updateValues(Map<String, Double> updatedValues) {
        LinkedHashMap<String, Double> newValues = new LinkedHashMap<>(values);
        newValues.putAll(updatedValues);
        values = newValues;
    }
}
