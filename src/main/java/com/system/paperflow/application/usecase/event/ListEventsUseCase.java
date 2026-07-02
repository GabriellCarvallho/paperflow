package com.system.paperflow.application.usecase.event;

import com.system.paperflow.application.persistence.EventPersistence;
import com.system.paperflow.domain.entity.Event;
import java.util.List;

public class ListEventsUseCase {

    private final EventPersistence eventPersistence;

    public ListEventsUseCase(EventPersistence eventPersistence) {
        this.eventPersistence = eventPersistence;
    }

    public List<Event> execute() {
        return eventPersistence.findAll();
    }
}
