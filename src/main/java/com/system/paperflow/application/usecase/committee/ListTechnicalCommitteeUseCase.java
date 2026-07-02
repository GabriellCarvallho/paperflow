package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.Researcher;

import java.util.List;

public class ListTechnicalCommitteeUseCase {

    private final CommitteePersistence committeePersistence;

    public ListTechnicalCommitteeUseCase(CommitteePersistence committeePersistence) {
        this.committeePersistence = committeePersistence;
    }

    public List<Researcher> execute() {
        return committeePersistence.findAcceptedReviewers();
    }
}
