package com.system.paperflow.application.usecase.topic;

import com.system.paperflow.application.persistence.TopicPersistence;
import com.system.paperflow.domain.entity.TopicComponent;

import java.util.List;

public class ListTopicTreeUseCase {

    private final TopicPersistence topicPersistence;

    public ListTopicTreeUseCase(TopicPersistence topicPersistence) {
        this.topicPersistence = topicPersistence;
    }

    public List<TopicComponent> execute() {
        return topicPersistence.findAllRoots();
    }
}
