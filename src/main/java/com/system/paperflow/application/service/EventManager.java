package com.system.paperflow.application.service;

import com.system.paperflow.domain.entity.Event;

public class EventManager {

    private Event currentEvent;

    public void startNewEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        this.currentEvent = event;
    }

    public Event getCurrentEvent() {
        if (currentEvent == null) {
            throw new IllegalStateException("No active event at the moment.");
        }
        return currentEvent;
    }

    public boolean hasActiveEvent() {
        return currentEvent != null;
    }
}