package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.User;

public class CoordinatorCreator extends UserCreator {

    @Override
    protected User createUser(String username, String email, String password, String institution) {
        return new Coordinator(username, email, password, institution);
    }
}
