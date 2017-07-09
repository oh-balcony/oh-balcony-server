package io.github.ohbalcony;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.ohbalcony.model.ControllerState;

@Service
public class ControllerManager {

    private static final Logger log = LoggerFactory.getLogger(ControllerManager.class);

    @Autowired
    private Store store;

    private final Map<String, ConnectedController> connectedControllers = new ConcurrentHashMap<>();

    private static class ConnectedController {
        public ControllerState state;
        public ControllerCallback callback;
    }

    public void receiveState(ControllerState state, ControllerCallback callback) {
        String controllerId = Objects.requireNonNull(state.controllerId);
        Objects.requireNonNull(callback);

        ConnectedController connectedController = new ConnectedController();
        connectedController.state = state;
        connectedController.callback = callback;

        connectedControllers.put(controllerId, connectedController);

        store.save(state);
    }

    public void updateState(ControllerState state) {
        String controllerId = Objects.requireNonNull(state.controllerId);

        ConnectedController connectedController = connectedControllers.get(controllerId);
        if (connectedController == null) {
            log.warn("Cannot update state for " + controllerId + ". Controller is not connected.");
            return;
        }

        connectedController.callback.send(state);

        connectedController.state.updateValues(state.values);

        store.save(state);
    }

    public ControllerState getState(String controllerId) {
        ConnectedController connectedController = connectedControllers.get(controllerId);
        if (connectedController == null)
            return null;
        return connectedController.state;
    }
}
