package io.github.ohbalcony;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ohbalcony.model.ControllerState;

public class ApplicationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ApplicationWebSocketHandler.class);

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private Store store;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection {} from: {}", session.getId(), session.getRemoteAddress().getHostName());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Closed connection {} ({})", session.getId(), session.getRemoteAddress().getHostName());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        ControllerState state = jsonMapper.readValue(message.getPayload(), ControllerState.class);

        // TODO remember session for this controllerId here

        store.save(state);
    }
}
