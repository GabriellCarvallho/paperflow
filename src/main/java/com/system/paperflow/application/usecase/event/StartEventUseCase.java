package com.system.paperflow.application.usecase.event;

import com.system.paperflow.application.event.EventManager;

import java.util.UUID;

public class StartEventUseCase {

    private final EventManager eventManager;

    public StartEventUseCase(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void execute(UUID eventId) {

    }
}
