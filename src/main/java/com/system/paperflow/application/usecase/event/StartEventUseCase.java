package com.system.paperflow.application.usecase.event;

import com.system.paperflow.application.persistence.EventPersistence;
import com.system.paperflow.domain.entity.Event;
import java.time.LocalDate;

public class StartEventUseCase {

    private final EventPersistence eventPersistence;

    public StartEventUseCase(EventPersistence eventPersistence) {
        this.eventPersistence = eventPersistence;
    }

    public Event execute(String name, String city, LocalDate startDate,
                         LocalDate endDate, LocalDate submissionDeadline) {
        Event event = new Event(name, city, startDate, endDate, submissionDeadline);
        eventPersistence.save(event);
        return event;
    }
}
