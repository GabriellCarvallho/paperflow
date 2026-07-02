package com.system.paperflow.application.usecase.event;

import java.time.LocalDate;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.EventCategory;

public class CreateEventUseCase {

    private final EventManager eventManager;

    public CreateEventUseCase(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public Event execute(String name,String city,LocalDate endDate,LocalDate submissionDeadline,EventCategory category) {

        Event event = new Event(
                name,
                city,
                LocalDate.now(),
                endDate,
                submissionDeadline,
                category
        );

        eventManager.setCurrentEvent(event);
        return event;
    }
}