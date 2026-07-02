package com.system.paperflow.application.event;

import com.system.paperflow.domain.entity.Event;

public class EventManager {

    private Event currentEvent;

    public void setCurrentEvent(Event event) {
        this.currentEvent = event;
    }

    public Event getCurrentEvent() {
        if (currentEvent == null) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }
        return currentEvent;
    }

    public boolean hasEvent() {
        return currentEvent != null;
    }

    public void clear() {
        currentEvent = null;
    }

}
