package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.Researcher;

public class ResearcherFactory {

    public Researcher create(String username, String email, String password, String institution) {
        return new Researcher(username, email, password, institution);
    }
}
