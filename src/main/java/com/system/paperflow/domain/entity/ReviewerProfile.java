package com.system.paperflow.domain.entity;

import java.util.Collections;
import java.util.Set;

public class ReviewerProfile implements Profile {

    private final Set<Topic> expertiseAreas;

    public ReviewerProfile(Set<Topic> expertiseAreas) {
        this.expertiseAreas = expertiseAreas;
    }

    @Override
    public String name() {
        return "REVIEWER";
    }

    public Set<Topic> getExpertiseAreas() {
        return Collections.unmodifiableSet(expertiseAreas);
    }
}
