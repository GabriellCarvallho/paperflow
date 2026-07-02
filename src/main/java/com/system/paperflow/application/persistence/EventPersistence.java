package com.system.paperflow.application.persistence;

import com.system.paperflow.domain.entity.Event;
import java.util.List;
import java.util.Optional;

public interface EventPersistence {

    void save(Event event);

    Optional<Event> findCurrentOpenEvent();

    List<Event> findAll();
}
