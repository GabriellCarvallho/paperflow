package com.system.paperflow.domain.entity;

import java.util.Set;

public class Reviewer extends Researcher {

    private Set<Topic> expertiseAreas;

    public Reviewer(String username, String email, String password, String institution, Set<Topic> expertiseAreas) {
        super(username, email, password, institution);
        this.expertiseAreas = expertiseAreas;
    }

    public Set<Topic> getExpertiseAreas() {
        return expertiseAreas;
    }
    
}
