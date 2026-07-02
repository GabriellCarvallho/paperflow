package com.system.paperflow.domain.entity;

import java.util.*;

public class Paper {

    private UUID id;
    private String title;
    private String summary;
    private Researcher author;
    private List<Researcher> collaborators;
    private Set<ThematicArea> areas;
    private Event event;

    private PaperStatus status;

    public Paper(String title, String summary, Researcher author, Event event) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.event = event;
        this.collaborators = new ArrayList<>();
        this.areas = new HashSet<>();
        this.status = PaperStatus.SUBMITTED;
    }

    public void markAsUnderReview() {
        this.status = PaperStatus.UNDER_REVIEW;
    }

    public void markAsAccepted() {
        this.status = PaperStatus.ACCEPTED;
    }

    public void markAsRejected() {
        this.status = PaperStatus.REJECTED;
    }

    public void addThematicArea(ThematicArea area) {

        if (!event.getThematicAreas().contains(area)) throw new IllegalArgumentException("A área temática não pertence ao evento.");

        areas.add(area);
    }

    public void addCollaborator(Researcher collaborator) {
        if (!collaborators.contains(collaborator)) {
            collaborators.add(collaborator);
        }
    }

    public void markUnderReview() {
        this.status = PaperStatus.UNDER_REVIEW;
    }

    public void accept() {
        this.status = PaperStatus.ACCEPTED;
    }

    public void reject() {
        this.status = PaperStatus.REJECTED;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Event getEvent() {
        return event;
    }

    public PaperStatus getStatus() {
        return status;
    }

    public Researcher getAuthor() {
        return author;
    }

    public List<Researcher> getCollaborators() {
        return collaborators;
    }

    public Set<ThematicArea> getAreas() {
        return areas;
    }

    public UUID getId() {
        return id;
    }
}