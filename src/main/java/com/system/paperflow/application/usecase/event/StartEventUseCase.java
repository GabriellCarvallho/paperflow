package com.system.paperflow.application.usecase.event;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.domain.entity.Event;

import java.util.UUID;

public class StartEventUseCase {

    private final EventManager eventManager;

    public StartEventUseCase(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void execute(UUID eventId) {
        if (!eventManager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        Event event = eventManager.getCurrentEvent();

        if (!event.getId().equals(eventId)) {
            throw new IllegalArgumentException("Evento não encontrado.");
        }

        event.start();
    }
}
