package com.system.paperflow.domain.entity;

public class Topic {

    private String name, description;

    public Topic(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
}
