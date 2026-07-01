package com.system.paperflow.domain.entity;

import java.util.List;
import java.util.Objects;

public class Topic implements TopicComponent {

    private final String name;
    private final String description;

    public Topic(String name, String description) {
        this.name = validateText(name, "nome da area tematica");
        this.description = validateText(description, "descricao da area tematica");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Topic> getKeywords() {
        return List.of(this);
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo " + fieldName + " e obrigatorio.");
        }

        return value.trim();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Topic topic)) {
            return false;
        }

        return name.equalsIgnoreCase(topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}