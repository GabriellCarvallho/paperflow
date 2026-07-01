package com.system.paperflow.domain.entity;

import java.util.List;

public interface TopicComponent {

    String getName();

    String getDescription();

    List<Topic> getKeywords();

    default int countKeywords() {
        return getKeywords().size();
    }
}
