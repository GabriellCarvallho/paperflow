package com.system.paperflow.domain.entity;

import com.system.paperflow.domain.state.PaperState;
import com.system.paperflow.domain.state.SubmittedPaperState;

import java.util.*;

public class Paper {

    private UUID id;
    private String title;
    private String summary;
    private Researcher author;
    private List<Researcher> collaborators;
    private Set<ThematicArea> areas;
    private Event event;

    private PaperState state;

    public Paper(String title, String summary, Researcher author, Event event) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.event = event;
        this.collaborators = new ArrayList<>();
        this.areas = new HashSet<>();
        this.state = new SubmittedPaperState();
    }

    public void markAsUnderReview() {
        state.markAsUnderReview(this);
    }

    public void markAsAccepted() {
        state.markAsAccepted(this);
    }

    public void markAsRejected() {
        state.markAsRejected(this);
    }

    public void changeState(PaperState state) {
        this.state = state;
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
        markAsUnderReview();
    }

    public void accept() {
        markAsAccepted();
    }

    public void reject() {
        markAsRejected();
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
        return state.status();
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
