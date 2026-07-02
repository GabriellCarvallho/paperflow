package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;

import java.util.Optional;

public class FindPendingCommitteeInvitationByReviewerEmailUseCase {

    private final CommitteePersistence committeePersistence;

    public FindPendingCommitteeInvitationByReviewerEmailUseCase(CommitteePersistence committeePersistence) {
        this.committeePersistence = committeePersistence;
    }

    public Optional<CommitteeInvitation> execute(String reviewerEmail) {
        return committeePersistence.findPendingInvitationByReviewerEmail(reviewerEmail.trim().toLowerCase());
    }
}
