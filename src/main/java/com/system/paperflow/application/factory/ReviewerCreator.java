package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.Reviewer;
import com.system.paperflow.domain.entity.User;

import java.util.Collections;

public class ReviewerCreator extends UserCreator {

    @Override
    protected User createUser(String username, String email, String password, String institution) {
        return new Reviewer(username, email, password, institution, Collections.emptySet());
    }
}
