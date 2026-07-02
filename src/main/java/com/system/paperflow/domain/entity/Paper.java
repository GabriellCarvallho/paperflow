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

    public Paper(String title, String summary, Researcher author, Event event) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.event = event;
        this.collaborators = new ArrayList<>();
        this.areas = new HashSet<>();
    }

    public void addThematicArea(ThematicArea area) {

        if (!event.getThematicAreas().contains(area)) throw new IllegalArgumentException("A área temática não pertence ao evento.");

        areas.add(area);
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