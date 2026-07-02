package com.system.paperflow.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event {

    private final UUID id;
    private final String name;
    private final String city;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate submissionDeadline;
    private final EventCategory category;

    private final List<ThematicArea> thematicAreas;
    private final List<Researcher> committee;

    private boolean started;

    public Event(String name, String city, LocalDate startDate, LocalDate endDate, LocalDate submissionDeadline, EventCategory category) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionDeadline = submissionDeadline;
        this.category = category;
        this.thematicAreas = new ArrayList<>();
        this.committee = new ArrayList<>();
        this.started = false;
    }

   public void start() {

        if (thematicAreas.isEmpty()) {
            throw new IllegalStateException("Event must have at least one thematic area");
        }

        if (committee.isEmpty()) {
            throw new IllegalStateException("Event must have at least one committee member");
        }

        this.started = true;
    }
    
    public void addThematicArea(ThematicArea area) {

        if (started) {
            throw new IllegalStateException("Event already started");
        }

        thematicAreas.add(area);
    }

    public void addReviewer(Researcher researcher) {

        if (started) {
            throw new IllegalStateException("Event already started");
        }

        if (!committee.contains(researcher)) {
            committee.add(researcher);
        }
    }

    public boolean isOpenForSubmission() {
        return started && !LocalDate.now().isAfter(submissionDeadline);
    }

    public boolean isStarted() {
        return started;
    }

    public UUID getId() {
        return id;
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

    public EventCategory getCategory() {
        return category;
    }

    public List<ThematicArea> getThematicAreas() {
        return thematicAreas;
    }

    public List<Researcher> getCommittee() {
        return committee;
    }

    public String getName() {
        return name;
    }
}
