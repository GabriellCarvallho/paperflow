package com.system.paperflow.application.persistence;

import com.system.paperflow.domain.entity.TopicComponent;

import java.util.List;
import java.util.Optional;

public interface TopicPersistence {

    void save(TopicComponent topicComponent);

    boolean existsByName(String name);

    Optional<TopicComponent> findByName(String name);

    List<TopicComponent> findAllRoots();
}