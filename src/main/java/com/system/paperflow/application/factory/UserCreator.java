package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.User;

public abstract class UserCreator {

    public User create(String username, String email, String password, String institution) {
        return createUser(username, email, password, institution);
    }

    protected abstract User createUser(String username, String email, String password, String institution);
}
