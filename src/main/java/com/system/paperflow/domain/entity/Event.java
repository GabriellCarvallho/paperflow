package com.system.paperflow.domain.entity;

import java.time.LocalDate;

public class Event {

    private final String name;
    private final String city;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate submissionDeadline;
    private boolean openForSubmissions;

    public Event(String name, String city, LocalDate startDate, LocalDate endDate,
                 LocalDate submissionDeadline) {
        this.name = name;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionDeadline = submissionDeadline;
        this.openForSubmissions = true;
    }

    public boolean isOpenForSubmissions() {
        return openForSubmissions && !LocalDate.now().isAfter(submissionDeadline);
    }

    public void closeSubmissions() {
        this.openForSubmissions = false;
    }

    public String getName() { 
        return name; 

    }
    public String getCity() { 
        return city; 

    }
    public LocalDate getStartDate() {
         return startDate; 

    }
    public LocalDate getEndDate() { 
        return endDate; 

    }
    public LocalDate getSubmissionDeadline() { 
        return submissionDeadline; 

    }
}