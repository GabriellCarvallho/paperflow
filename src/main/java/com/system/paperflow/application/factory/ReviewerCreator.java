package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ReviewerProfile;
import com.system.paperflow.domain.entity.Topic;

import java.util.Set;

public class ReviewerCreator {

    public Researcher assignReviewerProfile(Researcher researcher, Set<Topic> expertiseAreas) {
        researcher.addProfile(new ReviewerProfile(expertiseAreas));
        return researcher;
    }
}
