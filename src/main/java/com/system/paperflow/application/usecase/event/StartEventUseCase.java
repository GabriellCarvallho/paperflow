package com.system.paperflow.application.usecase.event;

import com.system.paperflow.domain.entity.Event;
import java.time.LocalDate;

public class StartEventUseCase {

    public Event execute(String name, String city, LocalDate startDate,
                         LocalDate endDate, LocalDate submissionDeadline) {
        return new Event(name, city, startDate, endDate, submissionDeadline);
    }
}
