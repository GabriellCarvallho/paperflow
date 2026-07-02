package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.Reviewer;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.User;

import java.util.Set;
import java.util.Collections;

public class ReviewerCreator extends UserCreator {

    @Override
    public User createUser(String username, String email, String password, String institution) {
        return new Reviewer(username, email, password, institution, Collections.emptySet());
    }

    public Reviewer createFromUser(User user, Set<Topic> expertiseAreas) {
        return new Reviewer(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getInstitution(),
                expertiseAreas
        );
    }
}