package io.github.ohbalcony.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ControllerState {
    public String controllerId;

    public Map<String, Double> values = new LinkedHashMap<>();
}
