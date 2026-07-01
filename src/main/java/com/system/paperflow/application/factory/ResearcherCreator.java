package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.User;

public class ResearcherCreator extends UserCreator {

    @Override
    protected User createUser(String username, String email, String password, String institution) {
        return new Researcher(username, email, password, institution);
    }
}
