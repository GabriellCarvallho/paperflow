package com.system.paperflow.application.usecase.topic;

import com.system.paperflow.application.exception.InvalidTopicDataException;
import com.system.paperflow.application.exception.UnauthorizedTopicManagementException;
import com.system.paperflow.application.persistence.TopicPersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.TopicComponent;

import java.util.HashSet;
import java.util.Set;

public class CreateTopicTreeUseCase {

    private final TopicPersistence topicPersistence;
    private final UserPersistence userPersistence;

    public CreateTopicTreeUseCase(TopicPersistence topicPersistence, UserPersistence userPersistence) {
        this.topicPersistence = topicPersistence;
        this.userPersistence = userPersistence;
    }

    public TopicComponent execute(String coordinatorEmail, TopicComponent topicComponent) {
        validateCoordinator(coordinatorEmail);
        validateTopicTree(topicComponent);

        topicPersistence.save(topicComponent);
        return topicComponent;
    }

    private void validateCoordinator(String coordinatorEmail) {
        String normalizedEmail = coordinatorEmail.trim().toLowerCase();

        userPersistence.findByEmail(normalizedEmail)
                .filter(Coordinator.class::isInstance)
                .orElseThrow(() -> new UnauthorizedTopicManagementException(
                        "Somente um coordenador cadastrado pode gerenciar areas tematicas."
                ));
    }

    private void validateTopicTree(TopicComponent topicComponent) {
        if (topicComponent.getKeywords().isEmpty()) {
            throw new InvalidTopicDataException("Um grupo de areas tematicas deve possuir pelo menos uma palavra-chave.");
        }

        validateDuplicatedKeywords(topicComponent.getKeywords());
    }

    private void validateDuplicatedKeywords(Iterable<Topic> topics) {
        Set<String> names = new HashSet<>();

        for (Topic topic : topics) {
            String normalizedName = topic.getName().trim().toLowerCase();

            if (!names.add(normalizedName)) {
                throw new InvalidTopicDataException("A palavra-chave '" + topic.getName() + "' foi informada mais de uma vez.");
            }
        }
    }
}
