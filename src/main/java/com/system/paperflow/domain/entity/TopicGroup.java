package com.system.paperflow.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopicGroup implements TopicComponent {

    private final String name;
    private final String description;
    private final List<TopicComponent> children;

    public TopicGroup(String name, String description) {
        this.name = validateText(name, "nome do grupo de areas tematicas");
        this.description = validateText(description, "descricao do grupo de areas tematicas");
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void addChild(TopicComponent child) {
        if (child == null) {
            throw new IllegalArgumentException("Nao e possivel adicionar uma area tematica nula.");
        }

        children.add(child);
    }

    public void removeChild(TopicComponent child) {
        children.remove(child);
    }

    public List<TopicComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public List<Topic> getKeywords() {
        List<Topic> keywords = new ArrayList<>();

        for (TopicComponent child : children) {
            keywords.addAll(child.getKeywords());
        }

        return Collections.unmodifiableList(keywords);
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo " + fieldName + " e obrigatorio.");
        }

        return value.trim();
    }
}
