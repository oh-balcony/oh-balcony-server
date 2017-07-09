package io.github.ohbalcony;

import io.github.ohbalcony.model.ControllerState;

public interface ControllerCallback {
    public void send(ControllerState state);
}
