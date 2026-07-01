package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;

import java.util.List;

public class ListCommitteeInvitationsUseCase {

    private final CommitteePersistence committeePersistence;

    public ListCommitteeInvitationsUseCase(CommitteePersistence committeePersistence) {
        this.committeePersistence = committeePersistence;
    }

    public List<CommitteeInvitation> execute() {
        return committeePersistence.findAllInvitations();
    }
}
