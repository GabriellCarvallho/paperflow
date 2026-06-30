package com.system.paperflow.application.builder;

import com.system.paperflow.domain.entity.Event;
import java.time.LocalDate;

public class EventBuilder {

    private String name;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate submissionDeadline;

    public EventBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EventBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public EventBuilder withPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public EventBuilder withSubmissionDeadline(LocalDate submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
        return this;
    }

    public Event build() {
        if (name == null || city == null || startDate == null
                || endDate == null || submissionDeadline == null) {
            throw new IllegalStateException("All event fields are required.");
        }
        return new Event(name, city, startDate, endDate, submissionDeadline);
    }
}