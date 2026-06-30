package com.system.paperflow.domain.entity;

import com.system.paperflow.domain.state.PaperState;
import com.system.paperflow.domain.state.SubmittedState;
import java.util.List;
import java.util.Set;

public class Paper {

    private String id;
    private String title;
    private String summary;
    private Researcher author;
    private List<Researcher> collaborators;
    private Set<Topic> topics;
    private Event event;
    private PaperState state;

    public Paper(String id, String title, String summary, Researcher author,
                 List<Researcher> collaborators, Set<Topic> topics, Event event) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.collaborators = collaborators;
        this.topics = topics;
        this.event = event;
        this.state = new SubmittedState();
    }

    public void advanceState() {
        state.advance(this);
    }

    public void setState(PaperState state) {
        this.state = state;
    }

    public PaperState getState() {
        return state;
    }

    public String getStatus() {
        return state.getStatus();
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Researcher getAuthor() {
        return author;
    }

    public List<Researcher> getCollaborators() {
        return collaborators;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public Event getEvent() {
        return event;
    }
}